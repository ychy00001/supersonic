package com.tencent.supersonic.chat.agent.tool;

import lombok.Data;

import java.util.List;

@Data
public class CwTool extends AgentTool {

    private List<Long> modelIds;

    private List<String> exampleQuestions;

}
