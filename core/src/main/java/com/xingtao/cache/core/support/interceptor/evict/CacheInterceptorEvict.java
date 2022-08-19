package com.xingtao.cache.core.support.interceptor.evict;

import com.xingtao.cache.api.cacheAPI.ICacheEvict;
import com.xingtao.cache.api.cacheAPI.ICacheInterceptor;
import com.xingtao.cache.api.cacheAPI.ICacheInterceptorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Description 驱除策略拦截器
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.interceptor.evict
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/16
 */
public class CacheInterceptorEvict<K,V> implements ICacheInterceptor<K, V> {

    private static final Logger log = LoggerFactory.getLogger(CacheInterceptorEvict.class);

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {

    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
        ICacheEvict<K,V> evict = context.cache().evict();

        Method method = context.method();
        final K key = (K) context.params()[0];
        if("remove".equals(method.getName())) {
            evict.removeKey(key);
        } else {
            evict.updateKey(key);
        }
    }
}
