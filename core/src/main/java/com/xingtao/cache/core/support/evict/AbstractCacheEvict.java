package com.xingtao.cache.core.support.evict;

import com.xingtao.cache.api.cacheAPI.ICacheEntry;
import com.xingtao.cache.api.cacheAPI.ICacheEvict;
import com.xingtao.cache.api.cacheAPI.ICacheEvictContext;

/**
 * @Description 抽象类——丢弃策略
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.evict
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/17
 */
public abstract class AbstractCacheEvict<K,V> implements ICacheEvict<K,V> {

    @Override
    public ICacheEntry<K,V> evict(ICacheEvictContext<K, V> context) {
        //3. 返回结果
        return doEvict(context);
    }

    /**
     * 执行移除
     * @param context 上下文
     * @return 结果
     */
    protected abstract ICacheEntry<K,V> doEvict(ICacheEvictContext<K, V> context);

    @Override
    public void updateKey(K key) {

    }

    @Override
    public void removeKey(K key) {

    }
}
