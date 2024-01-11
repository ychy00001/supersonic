package com.tencent.supersonic.chat.parser.cw;

import com.tencent.supersonic.chat.agent.NL2SQLTool;
import lombok.Data;

import java.util.List;

@Data
public class CwParserTool extends NL2SQLTool {

    private List<String> exampleQuestions;

}
