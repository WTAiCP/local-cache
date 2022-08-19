package com.xingtao.cache.core.support.proxy.cglib;

import com.xingtao.cache.api.cacheAPI.ICache;
import com.xingtao.cache.core.support.proxy.ICacheProxy;
import com.xingtao.cache.core.support.proxy.bs.CacheProxyBs;
import com.xingtao.cache.core.support.proxy.bs.CacheProxyBsContext;
import com.xingtao.cache.core.support.proxy.bs.ICacheProxyBsContext;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Description
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.proxy.cglib
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/17
 */
public class CglibProxy implements MethodInterceptor, ICacheProxy {

    /**
     * 被代理的对象
     */
    private final ICache target;

    public CglibProxy(ICache target) {
        this.target = target;
    }

    @Override
    public Object proxy() {
        Enhancer enhancer = new Enhancer();
        //目标对象类
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        //通过字节码技术创建目标对象类的子类实例作为代理
        return enhancer.create();
    }

    /**
     *
     * @param o
     * @param method
     * @param objects  相当于jdk代理中的params
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        ICacheProxyBsContext context = CacheProxyBsContext.newInstance()
                .method(method).params(objects).target(target);

        return CacheProxyBs.newInstance().context(context).execute();
    }
}
