package com.tencent.supersonic.chat.agent;

import lombok.Data;

import java.util.List;

@Data
public class LLMParserTool extends NL2SQLTool {

    private List<String> exampleQuestions;

}
