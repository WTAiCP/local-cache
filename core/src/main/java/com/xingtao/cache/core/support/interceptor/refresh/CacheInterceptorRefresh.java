package com.xingtao.cache.core.support.interceptor.refresh;

import com.xingtao.cache.api.cacheAPI.ICache;
import com.xingtao.cache.api.cacheAPI.ICacheInterceptor;
import com.xingtao.cache.api.cacheAPI.ICacheInterceptorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 刷新
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.interceptor.refresh
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/16
 */
public class CacheInterceptorRefresh<K,V> implements ICacheInterceptor<K,V> {

    private static final Logger log = LoggerFactory.getLogger(CacheInterceptorRefresh.class);

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
        log.debug("Refresh start");
        final ICache<K,V> cache = context.cache();
        cache.expire().refreshExpire(cache.keySet());
    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {

    }
}
