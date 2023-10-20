package com.tencent.supersonic.chat.parser.llm.cw;

import com.tencent.supersonic.chat.agent.tool.CwTool;
import com.tencent.supersonic.chat.agent.tool.DslTool;
import com.tencent.supersonic.chat.api.pojo.request.QueryReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CwParseResult {

    private CwReq cwReq;

    private CwResp cwResp;

    private QueryReq request;

    private CwTool cwTool;
}
