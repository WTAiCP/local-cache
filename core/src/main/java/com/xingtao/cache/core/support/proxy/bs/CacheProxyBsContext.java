package com.xingtao.cache.core.support.proxy.bs;

import com.xingtao.cache.api.annotation.CacheInterceptor;
import com.xingtao.cache.api.cacheAPI.ICache;

import java.lang.reflect.Method;

/**
 * @Description 代理引导类上下文
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.proxy.bs
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/16
 */
public class CacheProxyBsContext implements ICacheProxyBsContext {

    /**
     * 目标
     */
    private ICache target;

    /**
     * 输入参数
     */
    private Object[] params;

    /**
     * 方法
     */
    private Method method;

    /**
     * 拦截器
     */
    private CacheInterceptor interceptor;

    /**
     * 获取对象
     * @return
     */
    public static CacheProxyBsContext newInstance() {
        return new CacheProxyBsContext();
    }

    @Override
    public CacheInterceptor interceptor() {
        return interceptor;
    }

    @Override
    public ICache target() {
        return target;
    }

    @Override
    public ICacheProxyBsContext target(ICache target) {
        this.target = target;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public CacheProxyBsContext params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Method method() {
        return method;
    }

    public CacheProxyBsContext method(Method method) {
        this.method = method;
        //或者这个注解接口对象
        this.interceptor = method.getAnnotation(CacheInterceptor.class);
        return this;
    }

    /**
     * 执行目标方法（也即原汁原味的方法）
     * @return
     * @throws Throwable
     */
    @Override
    public Object process() throws Throwable {
        return this.method.invoke(target,params);
    }
}
