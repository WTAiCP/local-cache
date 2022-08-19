package com.xingtao.cache.core.support.proxy;

import com.github.houbb.heaven.support.proxy.none.NoneProxy;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.xingtao.cache.api.cacheAPI.ICache;
import com.xingtao.cache.core.support.proxy.cglib.CglibProxy;
import com.xingtao.cache.core.support.proxy.dynamic.DynamicProxy;

import java.lang.reflect.Proxy;

/**
 * @Description 获取对象代理
 * 1、空代理
 * 2、JDK代理
 * 3、cglib代理
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.proxy
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/9
 */
public class CacheProxy {
    private CacheProxy(){}

    public static <K,V> ICache<K,V> getProxy(final ICache<K,V> cache) {
        if (ObjectUtil.isNull(cache)) {
            return (ICache<K,V>) new NoneProxy(cache).proxy();
        }

        final Class clazz = cache.getClass();

        // 如果targetClass本身是个接口或者targetClass是JDK Proxy生成的,则使用JDK动态代理。
        // 参考 spring 的 AOP 判断
        if (clazz.isInterface() || Proxy.isProxyClass(clazz)) {
            return (ICache<K,V>) new DynamicProxy(cache).proxy();
        }

        return (ICache<K, V>) new CglibProxy(cache).proxy();
    }
}
