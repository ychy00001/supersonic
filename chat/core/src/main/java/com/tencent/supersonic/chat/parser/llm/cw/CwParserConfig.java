package com.tencent.supersonic.chat.parser.llm.cw;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class CwParserConfig {

    @Value("${cw.parser.url:}")
    private String url;

    @Value("${cw.parser.queryToDslPath:/queryToDsl}")
    private String queryToDslPath;

    @Value("${cw.parser.queryToDslPath:/queryToCw}")
    private String queryToCwPath;

    @Value("${cw.parser.vecSimilaritySearchPath:/vec_similarity_search}")
    private String vecSimilaritySearchPath;

    @Value("${cw.parser.dimValSimilaritySearchPath:/dim_val_similarity_search}")
    private String dimValSimilaritySearchPath;

    @Value("${cw.parser.funSimilaritySearchPath:/func_similarity_search}")
    private String funSimilaritySearchPath;
}
