package com.xingtao.cache.core.support.listener.remove;

import com.xingtao.cache.api.cacheAPI.ICacheRemoveListenerContext;

/**
 * @Description 删除的监听器
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.listener.remove
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/12
 */
public class CacheRemoveListenerContext<K,V> implements ICacheRemoveListenerContext<K,V> {
    /**
     * key
     */
    private K key;

    /**
     * 值
     */
    private V value;

    /**
     * 删除类型
     */
    private String type;

    /**
     * 新建实例
     * @param <K> key
     * @param <V> value
     * @return 结果
     */
    public static <K,V> CacheRemoveListenerContext<K,V> newInstance() {
        return new CacheRemoveListenerContext<>();
    }

    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }

    @Override
    public String type() {
        return type;
    }

    public CacheRemoveListenerContext<K, V> type(String type) {
        this.type = type;
        return this;
    }

    public CacheRemoveListenerContext<K, V> key(K key) {
        this.key = key;
        return this;
    }


    public CacheRemoveListenerContext<K, V> value(V value) {
        this.value = value;
        return this;
    }
}
