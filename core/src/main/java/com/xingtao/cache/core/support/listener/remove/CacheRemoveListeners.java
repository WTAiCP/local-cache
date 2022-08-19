package com.xingtao.cache.core.support.listener.remove;


import com.xingtao.cache.api.cacheAPI.ICacheRemoveListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存删除监听类
 * @author WT
 */
public class CacheRemoveListeners {

    private CacheRemoveListeners(){}

    /**
     * 默认监听类
     * @return 监听类列表
     * @param <K> key
     * @param <V> value
     */
    @SuppressWarnings("all")
    public static <K,V> List<ICacheRemoveListener<K,V>> defaults() {
        List<ICacheRemoveListener<K,V>> listeners = new ArrayList<>();
        listeners.add(new CacheRemoveListener());
        return listeners;
    }

}
