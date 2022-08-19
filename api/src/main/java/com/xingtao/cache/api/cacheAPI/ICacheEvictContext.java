package com.xingtao.cache.api.cacheAPI;

/**
 * 驱除策略  上下文
 *
 * 1. 新加的 key
 * 2. map 实现
 * 3. 淘汰监听器
 *
 * @author WT
 * @param <K> key
 * @param <V> value
 */
public interface ICacheEvictContext<K,V> {

    /**
     * 新加的 key
     * @return key
     */
    K key();

    /**
     * cache 实现
     * @return map
     */
    ICache<K, V> cache();

    /**
     * 获取大小
     * @return 大小
     */
    int size();

}
