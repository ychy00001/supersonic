package com.tencent.supersonic.headless.model.domain.listener;

import com.alibaba.fastjson.JSONObject;
import com.tencent.supersonic.common.pojo.DataEvent;
import com.tencent.supersonic.common.pojo.enums.DictWordType;
import com.tencent.supersonic.common.pojo.enums.EventType;
import com.tencent.supersonic.common.util.ComponentFactory;
import com.tencent.supersonic.common.util.embedding.EmbeddingQuery;
import com.tencent.supersonic.common.util.embedding.S2EmbeddingStore;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@Slf4j
public class MetaEmbeddingListener implements ApplicationListener<DataEvent> {

    public static final String COLLECTION_NAME = "meta_collection";

    private S2EmbeddingStore s2EmbeddingStore = ComponentFactory.getS2EmbeddingStore();

    @Value("${embedding.operation.sleep.time:3000}")
    private Integer embeddingOperationSleepTime;

    @Async
    @Override
    public void onApplicationEvent(DataEvent event) {
        if (CollectionUtils.isEmpty(event.getDataItems())) {
            return;
        }
        List<EmbeddingQuery> embeddingQueries = event.getDataItems()
                .stream()
                .map(dataItem -> {
                    EmbeddingQuery embeddingQuery = new EmbeddingQuery();
                    embeddingQuery.setQueryId(
                            dataItem.getId().toString() + DictWordType.NATURE_SPILT + dataItem.getType().getName());
                    embeddingQuery.setQuery(dataItem.getName());
                    Map meta = JSONObject.parseObject(JSONObject.toJSONString(dataItem), Map.class);
                    embeddingQuery.setMetadata(meta);
                    embeddingQuery.setQueryEmbedding(null);
                    return embeddingQuery;
                }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(embeddingQueries)) {
            return;
        }
        try {
            Thread.sleep(embeddingOperationSleepTime);
        } catch (InterruptedException e) {
            log.error("", e);
        }
        s2EmbeddingStore.addCollection(COLLECTION_NAME);
        if (event.getEventType().equals(EventType.ADD)) {
            s2EmbeddingStore.addQuery(COLLECTION_NAME, embeddingQueries);
        } else if (event.getEventType().equals(EventType.DELETE)) {
            s2EmbeddingStore.deleteQuery(COLLECTION_NAME, embeddingQueries);
        } else if (event.getEventType().equals(EventType.UPDATE)) {
            s2EmbeddingStore.deleteQuery(COLLECTION_NAME, embeddingQueries);
            s2EmbeddingStore.addQuery(COLLECTION_NAME, embeddingQueries);
        }
    }

}
