package com.xingtao.cache.core.support.proxy.none;

import com.xingtao.cache.core.support.proxy.ICacheProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Description 没有代理的情况
 * 直接再返回cache对象
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.proxy.none
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/12
 */
public class NoneProxy implements InvocationHandler, ICacheProxy {

    private final Object target;

    public NoneProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxy,args);
    }

    @Override
    public Object proxy() {
        return this.target;
    }
}
