package com.xingtao.cache.core.support.evict;

import com.xingtao.cache.api.cacheAPI.ICache;
import com.xingtao.cache.api.cacheAPI.ICacheEntry;
import com.xingtao.cache.api.cacheAPI.ICacheEvictContext;
import com.xingtao.cache.core.model.CacheEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description 驱除策略——最近最少使用（LRU）：经典LRU（也即待优化LRU）
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.evict
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/17
 */
public class CacheEvictLru<K,V> extends AbstractCacheEvict<K,V> {

    private static final Logger log = LoggerFactory.getLogger(CacheEvict.class);

    /**
     * 使用jdk中得Linkedlist作为实现LRU的链表
     */
    private final List<K> list = new LinkedList<>();

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K,V> result = null;
        final ICache<K,V> cache = context.cache();

        //如果超出size，则移除队尾元素
        if (cache.size() > context.size()) {
            K evictKey = list.get(list.size()-1);
            V evictValue = cache.remove(evictKey);
            result = new CacheEntry<>(evictKey, evictValue);
            //list.remove(list.size()-1);  ???
        }

        return result;
    }

    /**
     * 放入元素
     * 1、删除已经存在的
     * 2、新元素放到元素头部
     *
     * @param key
     */
    @Override
    public void updateKey(final K key) {
        this.list.remove(key);
        this.list.add(0, key);
    }

    /**
     * 移除元素
     * @param key
     */
    @Override
    public void removeKey(final K key) {
        this.list.remove(key);
    }
}
