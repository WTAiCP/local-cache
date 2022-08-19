package com.xingtao.cache.api.cacheAPI;

import java.util.Collection;

/**
 * @Description 缓存过期接口
 * 为了后期变于扩展，这里使用接口来定义过期功能
 * @Version
 * @BelongsPackage com.xingtao.cache.api.cacheAPI
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/9
 */
public interface ICacheExpire<K,V> {

    /**
     * 指定过期信息
     * @param key
     * @param expireAt 什么时候过期
     */
    void expire(final K key, final long expireAt);


    /**
     * 惰性删除需要处理的key
     * @param keyList
     */
    void refreshExpire(final Collection<K> keyList);

    /**
     * 待过期得key
     * 不存在则返回null
     * @param key  还没有过期的key
     * @return
     */
    Long expireTime(final K key);


}
