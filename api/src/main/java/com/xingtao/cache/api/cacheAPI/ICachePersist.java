package com.xingtao.cache.api.cacheAPI;

import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Version
 * @BelongsPackage com.xingtao.cache.api.cacheAPI
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/11
 */
public interface ICachePersist<K,V> {

    /**
     * 持久化缓存信息
     * @param cache
     */
    void persist(final ICache<K, V> cache);

    /**
     * 延迟时间
     * @return 延迟
     */
    long delay();

    /**
     * 时间间隔
     * @return 间隔
     */
    long period();

    /**
     * 时间单位
     * @return 时间单位
     */
    TimeUnit timeUnit();
}
