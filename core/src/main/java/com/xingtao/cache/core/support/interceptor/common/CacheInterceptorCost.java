package com.xingtao.cache.core.support.interceptor.common;

import com.github.houbb.heaven.util.util.CollectionUtil;
import com.xingtao.cache.api.cacheAPI.ICacheInterceptor;
import com.xingtao.cache.api.cacheAPI.ICacheInterceptorContext;
import com.xingtao.cache.api.cacheAPI.ICacheSlowListener;
import com.xingtao.cache.core.support.listener.slow.CacheSlowListenerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description 通用的拦截器，相当于AOP
 * 1、耗时记录
 * 2、慢日志
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.interceptor.common
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/16
 */
public class CacheInterceptorCost<K,V> implements ICacheInterceptor<K,V> {
    private static final Logger log = LoggerFactory.getLogger(CacheInterceptorCost.class);

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
        log.debug("Cost start, method: {}", context.method().getName());
    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
        //耗时记录
        long costMills = context.endMills()-context.startMills();
        final String methodName = context.method().getName();
        log.debug("Cost end, method: {}, cost: {}ms", methodName, costMills);

        //添加慢日志
        List<ICacheSlowListener> slowListeners = context.cache().slowListeners();
        if (CollectionUtil.isNotEmpty(slowListeners)) {
            CacheSlowListenerContext listenerContext = CacheSlowListenerContext.newInstance().startTimeMills(context.startMills())
                    .endTimeMills(context.endMills())
                    .costTimeMills(costMills)
                    .methodName(methodName)
                    .params(context.params())
                    .result(context.result());

            for(ICacheSlowListener slowListener : slowListeners) {
                long slowThanMills = slowListener.slowerThanMills();
                if(costMills >= slowThanMills) {
                    slowListener.listen(listenerContext);
                }
            }

        }

    }
}
