package com.xingtao.cache.core.support.proxy.dynamic;

import com.xingtao.cache.api.cacheAPI.ICache;
import com.xingtao.cache.core.support.proxy.ICacheProxy;
import com.xingtao.cache.core.support.proxy.bs.CacheProxyBs;
import com.xingtao.cache.core.support.proxy.bs.CacheProxyBsContext;
import com.xingtao.cache.core.support.proxy.bs.ICacheProxyBsContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description JDK动态代理
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.proxy.dynamic
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/12
 */
public class DynamicProxy implements InvocationHandler, ICacheProxy {

    /**
     * 被代理对象
     */
    private final ICache target;

    //固定写法
    public DynamicProxy(ICache target) {
        this.target = target;
    }

    /**
     * 获取代理对象
     * newProxyInsstance()
     * 固定写法
     * @return
     */
    @Override
    public Object proxy() {
        InvocationHandler handler = new DynamicProxy(target);

        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),handler);
    }


    /**
     * 这种方式虽然实现了异步执行，但是存在一个缺陷：
     * 强制用户返回值为 Future 的子类。
     * 如何实现不影响原来的值，要怎么实现呢？
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ICacheProxyBsContext context = CacheProxyBsContext.newInstance()
                .method(method).params(args).target(target);
        return CacheProxyBs.newInstance().context(context).execute();
    }
}
