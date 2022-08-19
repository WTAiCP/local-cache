package com.xingtao.cache.core.support.evict;


import com.xingtao.cache.api.cacheAPI.ICacheEntry;
import com.xingtao.cache.api.cacheAPI.ICacheEvictContext;

/**
 * 丢弃策略——无策略
 * @author WT
 */
public class CacheEvictNone<K,V> extends AbstractCacheEvict<K,V> {

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        return null;
    }

}
