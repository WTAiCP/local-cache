package com.xingtao.cache.core.support.proxy.bs;

import com.xingtao.cache.api.annotation.CacheInterceptor;
import com.xingtao.cache.api.cacheAPI.ICache;

import java.lang.reflect.Method;

/**
 * @Description 代理类上下文
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.proxy.bs
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/16
 */
public interface ICacheProxyBsContext {
    /**
     * 拦截器信息
     * @return 拦截器
     */
    CacheInterceptor interceptor();

    /**
     * 获取代理对象信息
     * @return 代理
     */
    ICache target();

    /**
     * 目标对象
     * @param target 对象
     * @return 结果
     */
    ICacheProxyBsContext target(final ICache target);

    /**
     * 参数信息
     * @return 参数信息
     */
    Object[] params();

    /**
     * 方法信息
     * @return 方法信息
     */
    Method method();

    /**
     * 方法执行
     * @return 执行
     * @throws Throwable 异常信息
     */
    Object process() throws Throwable;

}
