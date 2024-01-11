package com.tencent.supersonic.chat.parser.cw;

import com.tencent.supersonic.chat.agent.NL2SQLTool;
import com.tencent.supersonic.chat.api.pojo.request.QueryReq;
import com.tencent.supersonic.chat.query.llm.s2sql.LLMReq;
import com.tencent.supersonic.common.pojo.ModelCluster;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CwParseResult {

    private ModelCluster modelCluster;

    private CwReq cwReq;

    private CwResp cwResp;

    private QueryReq request;

    private NL2SQLTool commonAgentTool;

    private List<CwReq.ElementValue> linkingValues;
}
