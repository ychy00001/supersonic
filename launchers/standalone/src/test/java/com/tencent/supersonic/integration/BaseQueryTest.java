package com.tencent.supersonic.integration;

import com.tencent.supersonic.StandaloneLauncher;
import com.tencent.supersonic.chat.api.pojo.ChatContext;
import com.tencent.supersonic.chat.api.pojo.SchemaElement;
import com.tencent.supersonic.chat.api.pojo.SemanticParseInfo;
import com.tencent.supersonic.chat.api.pojo.request.ExecuteQueryReq;
import com.tencent.supersonic.chat.api.pojo.request.QueryReq;
import com.tencent.supersonic.chat.api.pojo.response.ParseResp;
import com.tencent.supersonic.chat.api.pojo.response.QueryResult;
import com.tencent.supersonic.chat.api.pojo.response.QueryState;
import com.tencent.supersonic.chat.service.AgentService;
import com.tencent.supersonic.chat.service.ChatService;
import com.tencent.supersonic.chat.service.ConfigService;
import com.tencent.supersonic.chat.service.QueryService;
import com.tencent.supersonic.util.DataUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StandaloneLauncher.class)
@ActiveProfiles("local")
public class BaseQueryTest {

    protected final int unit = 7;
    protected final String startDay = LocalDate.now().plusDays(-unit).toString();
    protected final String endDay = LocalDate.now().plusDays(-1).toString();
    protected final String period = "DAY";

    @Autowired
    @Qualifier("chatQueryService")
    protected QueryService queryService;
    @Autowired
    protected ChatService chatService;
    @Autowired
    protected ConfigService configService;
    @MockBean
    protected AgentService agentService;

    protected QueryResult submitMultiTurnChat(String queryText) throws Exception {
        ParseResp parseResp = submitParse(queryText);

        ExecuteQueryReq request = ExecuteQueryReq.builder()
                .queryId(parseResp.getQueryId())
                .parseId(parseResp.getSelectedParses().get(0).getId())
                .chatId(parseResp.getChatId())
                .queryText(parseResp.getQueryText())
                .user(DataUtils.getUser())
                .parseInfo(parseResp.getSelectedParses().get(0))
                .saveAnswer(true)
                .build();

        return queryService.performExecution(request);
    }

    protected QueryResult submitNewChat(String queryText) throws Exception {
        ParseResp parseResp = submitParse(queryText);

        ExecuteQueryReq request = ExecuteQueryReq.builder()
                .queryId(parseResp.getQueryId())
                .parseId(parseResp.getSelectedParses().get(0).getId())
                .chatId(parseResp.getChatId())
                .queryText(parseResp.getQueryText())
                .user(DataUtils.getUser())
                .parseInfo(parseResp.getSelectedParses().get(0))
                .saveAnswer(true)
                .build();

        QueryResult result = queryService.performExecution(request);

        ChatContext chatContext = chatService.getOrCreateContext(parseResp.getChatId());
        chatContext.setParseInfo(new SemanticParseInfo());
        chatService.updateContext(chatContext);

        return result;
    }

    protected ParseResp submitParse(String queryText) {
        QueryReq queryContextReq = DataUtils.getQueryContextReq(10, queryText);
        return queryService.performParsing(queryContextReq);
    }

    protected ParseResp submitParseWithAgent(String queryText, Integer agentId) {
        QueryReq queryContextReq = DataUtils.getQueryReqWithAgent(10, queryText, agentId);
        return queryService.performParsing(queryContextReq);
    }

    protected void assertSchemaElements(Set<SchemaElement> expected, Set<SchemaElement> actual) {
        Set<String> expectedNames = expected.stream().map(s -> s.getName())
                .filter(s -> s != null).collect(Collectors.toSet());
        Set<String> actualNames = actual.stream().map(s -> s.getName())
                .filter(s -> s != null).collect(Collectors.toSet());

        assertEquals(expectedNames, actualNames);
    }

    protected void assertQueryResult(QueryResult expected, QueryResult actual) {
        SemanticParseInfo expectedParseInfo = expected.getChatContext();
        SemanticParseInfo actualParseInfo = actual.getChatContext();

        assertEquals(QueryState.SUCCESS, actual.getQueryState());
        assertEquals(expected.getQueryMode(), actual.getQueryMode());
        assertEquals(expectedParseInfo.getAggType(), actualParseInfo.getAggType());

        assertSchemaElements(expectedParseInfo.getMetrics(), actualParseInfo.getMetrics());
        assertSchemaElements(expectedParseInfo.getDimensions(), actualParseInfo.getDimensions());

        assertEquals(expectedParseInfo.getDimensionFilters(), actualParseInfo.getDimensionFilters());
        assertEquals(expectedParseInfo.getMetricFilters(), actualParseInfo.getMetricFilters());

        assertEquals(expectedParseInfo.getDateInfo(), actualParseInfo.getDateInfo());
    }

}
