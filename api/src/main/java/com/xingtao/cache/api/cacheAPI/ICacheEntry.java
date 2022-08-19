package com.xingtao.cache.api.cacheAPI;

/**
 * 缓存详细信息
 * @author WT
 * @param <K> key
 * @param <V> value
 */
public interface ICacheEntry<K, V> {

    /**
     * @return key
     */
    K key();

    /**
     * @return value
     */
    V value();

}
