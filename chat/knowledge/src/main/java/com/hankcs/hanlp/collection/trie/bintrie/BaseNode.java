package com.hankcs.hanlp.collection.trie.bintrie;

import com.hankcs.hanlp.corpus.io.ByteArray;
import com.tencent.supersonic.common.util.ContextUtils;
import com.tencent.supersonic.knowledge.service.LoadRemoveService;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BaseNode<V> implements Comparable<BaseNode> {

    /**
     * 状态数组，方便读取的时候用
     */
    static final Status[] ARRAY_STATUS = Status.values();

    private static final Logger logger = LoggerFactory.getLogger(BaseNode.class);
    /**
     * 子节点
     */
    protected BaseNode[] child;
    /**
     * 节点状态
     */
    protected Status status;
    /**
     * 节点代表的字符
     */
    protected char c;
    /**
     * 节点代表的值
     */
    protected V value;

    protected String prefix = null;

    public BaseNode<V> transition(String path, int begin) {
        BaseNode<V> cur = this;
        for (int i = begin; i < path.length(); ++i) {
            cur = cur.getChild(path.charAt(i));
            if (cur == null || cur.status == Status.UNDEFINED_0) {
                return null;
            }
        }
        return cur;
    }

    public BaseNode<V> transition(char[] path, int begin) {
        BaseNode<V> cur = this;
        for (int i = begin; i < path.length; ++i) {
            cur = cur.getChild(path[i]);
            if (cur == null || cur.status == Status.UNDEFINED_0) {
                return null;
            }
        }
        return cur;
    }

    /**
     * 转移状态
     *
     * @param path
     * @return
     */
    public BaseNode<V> transition(char path) {
        BaseNode<V> cur = this;
        cur = cur.getChild(path);
        if (cur == null || cur.status == Status.UNDEFINED_0) {
            return null;
        }
        return cur;
    }

    /**
     * 添加子节点
     *
     * @return true-新增了节点 false-修改了现有节点
     */
    protected abstract boolean addChild(BaseNode node);

    /**
     * 是否含有子节点
     *
     * @param c 子节点的char
     * @return 是否含有
     */
    protected boolean hasChild(char c) {
        return getChild(c) != null;
    }

    protected char getChar() {
        return c;
    }

    /**
     * 获取子节点
     *
     * @param c 子节点的char
     * @return 子节点
     */
    public abstract BaseNode getChild(char c);

    /**
     * 获取节点对应的值
     *
     * @return 值
     */
    public final V getValue() {
        return value;
    }

    /**
     * 设置节点对应的值
     *
     * @param value 值
     */
    public final void setValue(V value) {
        this.value = value;
    }

    @Override
    public int compareTo(BaseNode other) {
        return compareTo(other.getChar());
    }

    /**
     * 重载，与字符的比较
     *
     * @param other
     * @return
     */
    public int compareTo(char other) {
        if (this.c > other) {
            return 1;
        }
        if (this.c < other) {
            return -1;
        }
        return 0;
    }

    /**
     * 获取节点的成词状态
     *
     * @return
     */
    public Status getStatus() {
        return status;
    }

    protected void walk(StringBuilder sb, Set<Map.Entry<String, V>> entrySet) {
        sb.append(c);
        if (status == Status.WORD_MIDDLE_2 || status == Status.WORD_END_3) {
            entrySet.add(new TrieEntry(sb.toString(), value));
        }
        if (child == null) {
            return;
        }
        for (BaseNode node : child) {
            if (node == null) {
                continue;
            }
            node.walk(new StringBuilder(sb.toString()), entrySet);
        }
    }

    protected void walkToSave(DataOutputStream out) throws IOException {
        out.writeChar(c);
        out.writeInt(status.ordinal());
        int childSize = 0;
        if (child != null) {
            childSize = child.length;
        }
        out.writeInt(childSize);
        if (child == null) {
            return;
        }
        for (BaseNode node : child) {
            node.walkToSave(out);
        }
    }

    protected void walkToSave(ObjectOutput out) throws IOException {
        out.writeChar(c);
        out.writeInt(status.ordinal());
        if (status == Status.WORD_END_3 || status == Status.WORD_MIDDLE_2) {
            out.writeObject(value);
        }
        int childSize = 0;
        if (child != null) {
            childSize = child.length;
        }
        out.writeInt(childSize);
        if (child == null) {
            return;
        }
        for (BaseNode node : child) {
            node.walkToSave(out);
        }
    }

    protected void walkToLoad(ByteArray byteArray, _ValueArray<V> valueArray) {
        c = byteArray.nextChar();
        status = ARRAY_STATUS[byteArray.nextInt()];
        if (status == Status.WORD_END_3 || status == Status.WORD_MIDDLE_2) {
            value = valueArray.nextValue();
        }
        int childSize = byteArray.nextInt();
        child = new BaseNode[childSize];
        for (int i = 0; i < childSize; ++i) {
            child[i] = new Node<V>();
            child[i].walkToLoad(byteArray, valueArray);
        }
    }

    protected void walkToLoad(ObjectInput byteArray) throws IOException, ClassNotFoundException {
        c = byteArray.readChar();
        status = ARRAY_STATUS[byteArray.readInt()];
        if (status == Status.WORD_END_3 || status == Status.WORD_MIDDLE_2) {
            value = (V) byteArray.readObject();
        }
        int childSize = byteArray.readInt();
        child = new BaseNode[childSize];
        for (int i = 0; i < childSize; ++i) {
            child[i] = new Node<V>();
            child[i].walkToLoad(byteArray);
        }
    }

    public enum Status {
        /**
         * 未指定，用于删除词条
         */
        UNDEFINED_0,
        /**
         * 不是词语的结尾
         */
        NOT_WORD_1,
        /**
         * 是个词语的结尾，并且还可以继续
         */
        WORD_MIDDLE_2,
        /**
         * 是个词语的结尾，并且没有继续
         */
        WORD_END_3,
    }

    public class TrieEntry extends AbstractMap.SimpleEntry<String, V> implements Comparable<TrieEntry> {

        public TrieEntry(String key, V value) {
            super(key, value);
        }

        @Override
        public int compareTo(TrieEntry o) {
            return getKey().compareTo(String.valueOf(o.getKey()));
        }
    }

    @Override
    public String toString() {
        return "BaseNode{"
                + "child="
                + Arrays.toString(child)
                + ", status="
                + status
                + ", c="
                + c
                + ", value="
                + value
                + ", prefix='"
                + prefix
                + '\''
                + '}';
    }

    public void walkNode(Set<Map.Entry<String, V>> entrySet, Integer agentId, Set<Long> detectModelIds) {
        if (status == Status.WORD_MIDDLE_2 || status == Status.WORD_END_3) {
            LoadRemoveService loadRemoveService = ContextUtils.getBean(LoadRemoveService.class);
            logger.debug("agentId:{},detectModelIds:{},before:{}", agentId, detectModelIds, value.toString());
            List natures = loadRemoveService.removeNatures((List) value, agentId, detectModelIds);
            String name = this.prefix != null ? this.prefix + c : "" + c;
            logger.debug("name:{},after:{},natures:{}", name, (List) value, natures);
            entrySet.add(new TrieEntry(name, (V) natures));
        }
    }

    /***
     * walk limit
     * @param sb
     * @param entrySet
     * @param limit
     */
    public void walkLimit(StringBuilder sb, Set<Map.Entry<String, V>> entrySet, int limit, Integer agentId,
            Set<Long> detectModelIds) {
        Queue<BaseNode> queue = new ArrayDeque<>();
        this.prefix = sb.toString();
        queue.add(this);
        while (!queue.isEmpty()) {
            if (entrySet.size() >= limit) {
                break;
            }
            BaseNode root = queue.poll();
            if (root == null) {
                continue;
            }
            root.walkNode(entrySet, agentId, detectModelIds);
            if (root.child == null) {
                continue;
            }
            String prefix = root.prefix + root.c;
            for (BaseNode node : root.child) {
                if (Objects.nonNull(node)) {
                    node.prefix = prefix;
                    queue.add(node);
                }
            }
        }
    }

}
