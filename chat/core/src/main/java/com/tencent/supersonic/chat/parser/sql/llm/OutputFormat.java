package com.tencent.supersonic.chat.parser.sql.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.supersonic.chat.parser.plugin.function.FunctionResp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

/***
 * output format
 */
@Slf4j
public class OutputFormat {

    public static final String PATTERN = "\\{[^{}]+\\}";

    public static String getSchemaLink(String schemaLink) {
        String reult = "";
        try {
            reult = schemaLink.trim();
            String pattern = "Schema_links:(.*)";
            Pattern regexPattern = Pattern.compile(pattern, Pattern.DOTALL);
            Matcher matcher = regexPattern.matcher(reult);
            if (matcher.find()) {
                return matcher.group(1).trim();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return reult;
    }

    public static String getSql(String sqlOutput) {
        String sql = "";
        try {
            sqlOutput = sqlOutput.trim();
            String pattern = "SQL:(.*)";
            Pattern regexPattern = Pattern.compile(pattern);
            Matcher matcher = regexPattern.matcher(sqlOutput);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return sql;
    }

    public static String getSchemaLinks(String text) {
        String schemaLinks = "";
        try {
            text = text.trim();
            String pattern = "Schema_links:(\\[.*?\\])|Schema_links: (\\[.*?\\])";
            Pattern regexPattern = Pattern.compile(pattern);
            Matcher matcher = regexPattern.matcher(text);

            if (matcher.find()) {
                if (matcher.group(1) != null) {
                    schemaLinks = matcher.group(1);
                } else if (matcher.group(2) != null) {
                    schemaLinks = matcher.group(2);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }

        return schemaLinks;
    }

    public static Pair<String, Map<String, Double>> selfConsistencyVote(List<String> inputList) {
        Map<String, Integer> inputCounts = new HashMap<>();
        for (String input : inputList) {
            inputCounts.put(input, inputCounts.getOrDefault(input, 0) + 1);
        }

        String inputMax = null;
        int maxCount = 0;
        int inputSize = inputList.size();
        Map<String, Double> votePercentage = new HashMap<>();
        for (Map.Entry<String, Integer> entry : inputCounts.entrySet()) {
            String input = entry.getKey();
            int count = entry.getValue();
            if (count > maxCount) {
                inputMax = input;
                maxCount = count;
            }
            double percentage = (double) count / inputSize;
            votePercentage.put(input, percentage);
        }
        return Pair.of(inputMax, votePercentage);
    }

    public static List<String> formatList(List<String> toFormatList) {
        List<String> results = new ArrayList<>();
        for (String toFormat : toFormatList) {
            List<String> items = new ArrayList<>();
            String[] split = toFormat.replace("[", "").replace("]", "").split(",");
            for (String item : split) {
                items.add(item.trim());
            }
            Collections.sort(items);
            String result = "[" + String.join(",", items) + "]";
            results.add(result);
        }
        return results;
    }

    public static FunctionResp functionCallParse(String llmOutput) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(llmOutput);
            String selectedTool = jsonNode.get("选择工具").asText();
            FunctionResp resp = new FunctionResp();
            resp.setToolSelection(selectedTool);
            return resp;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }
}
