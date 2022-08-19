package com.xingtao.cache.api.cacheAPI;

/**
 * @Description 缓存接口
 * @Version
 * @BelongsPackage com.xingtao.cache.api.cacheAPI
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/11
 */
public interface ICacheLoad<K,V> {

    /**
     * 加载缓存信息
     * @param cache
     */
    void load(final ICache<K,V> cache);
}
