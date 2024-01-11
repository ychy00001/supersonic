package com.tencent.supersonic.knowledge.service;

import com.hankcs.hanlp.collection.trie.bintrie.BaseNode;
import com.hankcs.hanlp.collection.trie.bintrie.BinTrie;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import com.tencent.supersonic.knowledge.dictionary.DictWord;
import com.tencent.supersonic.common.pojo.enums.DictWordType;
import com.tencent.supersonic.knowledge.dictionary.DictionaryAttributeUtil;
import com.tencent.supersonic.knowledge.dictionary.HanlpMapResult;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class SearchService {

    public static final int SEARCH_SIZE = 200;
    private static BinTrie<List<String>> trie;
    private static BinTrie<List<String>> suffixTrie;

    static {
        trie = new BinTrie<>();
        suffixTrie = new BinTrie<>();
    }

    /***
     * prefix Search
     * @param key
     * @return
     */
    public static List<HanlpMapResult> prefixSearch(String key, int limit, Integer agentId, Set<Long> detectModelIds) {
        return prefixSearch(key, limit, agentId, trie, detectModelIds);
    }

    public static List<HanlpMapResult> prefixSearch(String key, int limit, Integer agentId,
            BinTrie<List<String>> binTrie, Set<Long> detectModelIds) {
        Set<Map.Entry<String, List<String>>> result = prefixSearchLimit(key, limit, binTrie, agentId, detectModelIds);
        return result.stream().map(
                        entry -> {
                            String name = entry.getKey().replace("#", " ");
                            return new HanlpMapResult(name, entry.getValue(), key);
                        }
                ).sorted((a, b) -> -(b.getName().length() - a.getName().length()))
                .limit(SEARCH_SIZE)
                .collect(Collectors.toList());
    }

    /***
     * suffix Search
     * @param key
     * @return
     */
    public static List<HanlpMapResult> suffixSearch(String key, int limit, Integer agentId, Set<Long> detectModelIds) {
        String reverseDetectSegment = StringUtils.reverse(key);
        return suffixSearch(reverseDetectSegment, limit, agentId, suffixTrie, detectModelIds);
    }

    public static List<HanlpMapResult> suffixSearch(String key, int limit, Integer agentId,
            BinTrie<List<String>> binTrie, Set<Long> detectModelIds) {
        Set<Map.Entry<String, List<String>>> result = prefixSearchLimit(key, limit, binTrie, agentId, detectModelIds);
        return result.stream().map(
                        entry -> {
                            String name = entry.getKey().replace("#", " ");
                            List<String> natures = entry.getValue().stream()
                                    .map(nature -> nature.replaceAll(DictWordType.SUFFIX.getType(), ""))
                                    .collect(Collectors.toList());
                            name = StringUtils.reverse(name);
                            return new HanlpMapResult(name, natures, key);
                        }
                ).sorted((a, b) -> -(b.getName().length() - a.getName().length()))
                .limit(SEARCH_SIZE)
                .collect(Collectors.toList());
    }

    private static Set<Map.Entry<String, List<String>>> prefixSearchLimit(String key, int limit,
            BinTrie<List<String>> binTrie, Integer agentId, Set<Long> detectModelIds) {
        key = key.toLowerCase();
        Set<Map.Entry<String, List<String>>> entrySet = new TreeSet<Map.Entry<String, List<String>>>();

        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(key)) {
            sb = new StringBuilder(key.substring(0, key.length() - 1));
        }
        BaseNode branch = binTrie;
        char[] chars = key.toCharArray();
        for (char aChar : chars) {
            if (branch == null) {
                return entrySet;
            }
            branch = branch.getChild(aChar);
        }

        if (branch == null) {
            return entrySet;
        }
        branch.walkLimit(sb, entrySet, limit, agentId, detectModelIds);
        return entrySet;
    }

    public static void clear() {
        log.info("clear all trie");
        trie = new BinTrie<>();
        suffixTrie = new BinTrie<>();
    }

    public static void put(String key, CoreDictionary.Attribute attribute) {
        trie.put(key, getValue(attribute.nature));
    }

    public static void loadSuffix(List<DictWord> suffixes) {
        if (CollectionUtils.isEmpty(suffixes)) {
            return;
        }
        TreeMap<String, CoreDictionary.Attribute> map = new TreeMap();
        for (DictWord suffix : suffixes) {
            CoreDictionary.Attribute attributeNew = suffix.getNatureWithFrequency() == null
                    ? new CoreDictionary.Attribute(Nature.nz, 1)
                    : CoreDictionary.Attribute.create(suffix.getNatureWithFrequency());
            if (map.containsKey(suffix.getWord())) {
                attributeNew = DictionaryAttributeUtil.getAttribute(map.get(suffix.getWord()), attributeNew);
            }
            map.put(suffix.getWord(), attributeNew);
        }
        for (Map.Entry<String, CoreDictionary.Attribute> stringAttributeEntry : map.entrySet()) {
            putSuffix(stringAttributeEntry.getKey(), stringAttributeEntry.getValue());
        }
    }

    public static void putSuffix(String key, CoreDictionary.Attribute attribute) {
        Nature[] nature = attribute.nature;
        suffixTrie.put(key, getValue(nature));
    }

    private static List<String> getValue(Nature[] nature) {
        return Arrays.stream(nature).map(entry -> entry.toString()).collect(Collectors.toList());
    }

    public static void remove(DictWord dictWord, Nature[] natures) {
        trie.remove(dictWord.getWord());
        if (Objects.nonNull(natures) && natures.length > 0) {
            trie.put(dictWord.getWord(), getValue(natures));
        }
        if (dictWord.getNature().contains(DictWordType.METRIC.getType()) || dictWord.getNature()
                .contains(DictWordType.DIMENSION.getType())) {
            suffixTrie.remove(dictWord.getWord());
        }
    }
}

