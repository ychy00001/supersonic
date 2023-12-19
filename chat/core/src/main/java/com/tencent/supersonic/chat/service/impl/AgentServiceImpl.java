package com.tencent.supersonic.chat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.tencent.supersonic.auth.api.authentication.pojo.User;
import com.tencent.supersonic.auth.api.authorization.enmus.AuthObjTypeEnum;
import com.tencent.supersonic.auth.api.authorization.response.AuthObjectResp;
import com.tencent.supersonic.auth.api.authorization.service.AuthService;
import com.tencent.supersonic.chat.agent.Agent;
import com.tencent.supersonic.chat.agent.tool.AgentToolType;
import com.tencent.supersonic.chat.agent.tool.CwTool;
import com.tencent.supersonic.chat.agent.tool.CommonAgentTool;
import com.tencent.supersonic.chat.persistence.dataobject.AgentDO;
import com.tencent.supersonic.chat.persistence.repository.AgentRepository;
import com.tencent.supersonic.chat.service.AgentService;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class AgentServiceImpl implements AgentService {

    private AgentRepository agentRepository;
    private AuthService authService;

    public AgentServiceImpl(AgentRepository agentRepository, AuthService authService) {
        this.agentRepository = agentRepository;
        this.authService = authService;
    }

    @Override
    public List<Agent> getAgents() {
        return getAgentDOList().stream()
                .map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<Agent> getAgents(User user) {
        List<Agent> agentList = getAgentDOList().stream()
                .map(this::convert).collect(Collectors.toList());
        if (user.getName().equals("admin") || user.getIsAdmin() == 1) {
            return agentList;
        }
        AuthObjectResp authObjectResp = authService.queryAuthorizedObj(user, AuthObjTypeEnum.AGENT);
        List<String> obIds = authObjectResp.getObjIds();
        return agentList.stream().filter(item -> obIds.contains(String.valueOf(item.getId()))).collect(Collectors.toList());
    }

    @Override
    public void createAgent(Agent agent, User user) {
        agentRepository.createAgent(convert(agent, user));
    }

    @Override
    public void updateAgent(Agent agent, User user) {
        agentRepository.updateAgent(convert(agent, user));
    }

    @Override
    public Agent getAgent(Integer id) {
        if (id == null) {
            return null;
        }
        return convert(agentRepository.getAgent(id));
    }

    @Override
    public void deleteAgent(Integer id) {
        agentRepository.deleteAgent(id);
    }

    private List<AgentDO> getAgentDOList() {
        return agentRepository.getAgents();
    }

    private Agent convert(AgentDO agentDO) {
        if (agentDO == null) {
            return null;
        }
        Agent agent = new Agent();
        BeanUtils.copyProperties(agentDO, agent);
        agent.setAgentConfig(agentDO.getConfig());
        agent.setExamples(JSONObject.parseArray(agentDO.getExamples(), String.class));
        return agent;
    }

    private AgentDO convert(Agent agent, User user) {
        AgentDO agentDO = new AgentDO();
        BeanUtils.copyProperties(agent, agentDO);
        agentDO.setConfig(agent.getAgentConfig());
        agentDO.setExamples(JSONObject.toJSONString(agent.getExamples()));
        agentDO.setCreatedAt(new Date());
        agentDO.setCreatedBy(user.getName());
        agentDO.setUpdatedAt(new Date());
        agentDO.setUpdatedBy(user.getName());
        if (agentDO.getStatus() == null) {
            agentDO.setStatus(1);
        }
        return agentDO;
    }

    public List<CommonAgentTool> getParserTools(Integer agentId, AgentToolType agentToolType) {
        Agent agent = getAgent(agentId);
        if (agent == null) {
            return Lists.newArrayList();
        }
        List<String> tools = agent.getTools(agentToolType);
        if (CollectionUtils.isEmpty(tools)) {
            return Lists.newArrayList();
        }
        return tools.stream().map(tool -> JSONObject.parseObject(tool, CommonAgentTool.class))
                .collect(Collectors.toList());
    }

    public Set<Long> getModelIds(Integer agentId, AgentToolType agentToolType) {
        List<CommonAgentTool> commonAgentTools = getParserTools(agentId, agentToolType);
        if (CollectionUtils.isEmpty(commonAgentTools)) {
            return new HashSet<>();
        }
        return commonAgentTools.stream().map(CommonAgentTool::getModelIds)
                .filter(modelIds -> !CollectionUtils.isEmpty(modelIds))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public List<CwTool> getCwTools(Integer agentId, AgentToolType agentToolType) {
        Agent agent = getAgent(agentId);
        if (agent == null) {
            return Lists.newArrayList();
        }
        List<String> tools = agent.getTools(agentToolType);
        if (CollectionUtils.isEmpty(tools)) {
            return Lists.newArrayList();
        }
        return tools.stream().map(tool -> JSONObject.parseObject(tool, CwTool.class)).collect(Collectors.toList());
    }

    @Override
    public Set<Long> getCwToolsModelIds(Integer agentId, AgentToolType agentToolType) {
        List<CwTool> dslTools = getCwTools(agentId, agentToolType);
        if (CollectionUtils.isEmpty(dslTools)) {
            return new HashSet<>();
        }
        return dslTools.stream().map(CwTool::getModelIds)
                .filter(modelIds -> !CollectionUtils.isEmpty(modelIds))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean containsAllModel(Set<Long> detectModelIds) {
        return !CollectionUtils.isEmpty(detectModelIds) && detectModelIds.contains(-1L);
    }
}
