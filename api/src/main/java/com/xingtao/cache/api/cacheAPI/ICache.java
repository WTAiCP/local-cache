package com.xingtao.cache.api.cacheAPI;

import java.util.List;
import java.util.Map;

/**
 * @Description 缓存接口
 * @Version
 * @BelongsPackage com.xingtao.cache.api.cacheAPI
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/5/9
 */
public interface  ICache<K,V> extends Map<K,V> {

    /**
     * 1、定时删除
     *      （1）
     * 2、惰性删除
     *      （1）
     * @param key
     * @param timeInMills 毫秒
     * @return
     */
    ICache<K,V> expire(final K key, final long timeInMills);

    /**
     * 在指定的时间过期
     * @param key
     * @param timeInMills
     * @return
     */
    ICache<K,V> expireAt(final K key, final long timeInMills);

    /**
     * 获取缓存的过期处理类
     * @return 处理类实现
     */
    ICacheExpire<K,V> expire();

    /**
     * 删除监听类列表
     * @return 监听器列表
     */
    List<ICacheRemoveListener<K,V>> removeListeners();

    /**
     * 慢日志监听类列表
     * @return 监听器列表
     */
    List<ICacheSlowListener> slowListeners();

    /**
     * 加载信息
     * @return 加载信息
     */
    ICacheLoad<K,V> load();

    /**
     * 持久化类
     * @return 持久化类
     */
    ICachePersist<K,V> persist();

    /**
     * 淘汰策略
     * @return 淘汰
     */
    ICacheEvict<K,V> evict();
}
