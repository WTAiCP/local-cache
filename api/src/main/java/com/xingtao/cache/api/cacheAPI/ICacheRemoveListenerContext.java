package com.xingtao.cache.api.cacheAPI;

/**
 * 删除监听器上下文
 *
 * （1）耗时统计
 * （2）监听器
 *
 * @author WT
 * @since 0.0.6
 * @param <K> key
 * @param <V> value
 */
public interface ICacheRemoveListenerContext<K,V> {

    /**
     * 清空的 key
     * @return key
     */
    K key();

    /**
     * 值
     * @return 值
     */
    V value();

    /**
     * 删除类型
     * @return 类型
     */
    String type();

}
