package com.xingtao.cache.core.support.evict;

import com.xingtao.cache.api.cacheAPI.ICache;
import com.xingtao.cache.api.cacheAPI.ICacheEntry;
import com.xingtao.cache.api.cacheAPI.ICacheEvictContext;
import com.xingtao.cache.core.model.CacheEntry;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Description 先进先出
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.evict
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/17
 */
public class CacheEvictFifo<K,V> extends AbstractCacheEvict<K,V>{

    /**
     * 队列
     */
    private final Queue<K> queue = new LinkedList<>();

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        CacheEntry<K,V> result = null;

        final ICache<K,V> cache = context.cache();
        // 超过限制，执行移除
        if(cache.size() >= context.size()) {
            K evictKey = queue.remove();
            // 移除最开始的元素
            V evictValue = cache.remove(evictKey);
            result = new CacheEntry<>(evictKey, evictValue);
        }

        // 将新加的元素放入队尾
        final K key = context.key();
        queue.add(key);

        return result;
    }
}
