package com.xingtao.cache.core.support.persist;

import com.xingtao.cache.api.cacheAPI.ICache;
import com.xingtao.cache.api.cacheAPI.ICachePersist;

import java.util.concurrent.TimeUnit;

/**
 * 缓存持久化-无任何操作
 * @author WT
 * @since 0.0.8
 */
public class CachePersistNone<K,V> implements ICachePersist<K, V> {
    @Override
    public void persist(ICache<K, V> cache) {

    }

    @Override
    public long delay() {
        return 0;
    }

    @Override
    public long period() {
        return 0;
    }

    @Override
    public TimeUnit timeUnit() {
        return null;
    }
}
