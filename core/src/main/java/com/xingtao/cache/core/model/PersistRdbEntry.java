package com.xingtao.cache.core.model;

/**
 * 持久化明细
 * @author WT
 * @since 0.0.8
 */
public class PersistRdbEntry<K,V> {

    /**
     * key
     */
    private K key;

    /**
     * value
     */
    private V value;

    /**
     * expire
     */
    private Long expire;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }
}
