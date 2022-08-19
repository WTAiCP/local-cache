package com.xingtao.cache.api.cacheAPI;

/**
 * 删除监听器接口
 *
 * @author WT
 * @since 0.0.6
 * @param <K> key
 * @param <V> value
 */
public interface ICacheRemoveListener<K,V> {

    /**
     * 监听
     * @param context 上下文
     */
    void listen(final ICacheRemoveListenerContext<K,V> context);

}
