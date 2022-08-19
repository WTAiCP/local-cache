package com.xingtao.cache.core.support.proxy.bs;

import com.xingtao.cache.api.annotation.CacheInterceptor;
import com.xingtao.cache.api.cacheAPI.ICache;
import com.xingtao.cache.api.cacheAPI.ICacheInterceptor;
import com.xingtao.cache.api.cacheAPI.ICachePersist;
import com.xingtao.cache.core.support.interceptor.CacheInterceptorContext;
import com.xingtao.cache.core.support.interceptor.CacheInterceptors;
import com.xingtao.cache.core.support.persist.CachePersistAof;

import java.util.List;

/**
 * @Description 代理引导类
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.proxy.bs
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/16
 */
public class CacheProxyBs {

    private CacheProxyBs() {}

    /**
     * 代理上下文
     */
    private ICacheProxyBsContext context;

    /**
     * 默认通用拦截器
     *
     * JDK 的泛型擦除导致这里不能使用泛型
     */
    @SuppressWarnings("all")
    private final List<ICacheInterceptor> commonInterceptors = CacheInterceptors.defaultCommonList();

    /**
     * 默认刷新拦截器
     */
    @SuppressWarnings("all")
    private final List<ICacheInterceptor> refreshInterceptors = CacheInterceptors.defaultRefreshList();

    /**
     * 持久化拦截器
     */
    @SuppressWarnings("all")
    private final ICacheInterceptor persistInterceptors = CacheInterceptors.aof();

    /**
     * 驱除拦截器
     */
    @SuppressWarnings("all")
    private final ICacheInterceptor evictInterceptors = CacheInterceptors.evict();

    /**
     * 新建对象实例
     * @return 实例
     */
    public static CacheProxyBs newInstance() {
        return new CacheProxyBs();
    }

    public CacheProxyBs context(ICacheProxyBsContext context) {
        this.context = context;
        return this;
    }

    /**
     * 执行方法
     * 重要
     * @return
     */
    public Object execute() throws Throwable {
        //开始的时间
        final long startMills = System.currentTimeMillis();
        final ICache cache = context.target();
        //获取context的上下文,然后方入interceptor的上下文中
        CacheInterceptorContext interceptorContext = CacheInterceptorContext.newInstance()
                .startMills(startMills)
                .method(context.method())
                .params(context.params())
                .cache(context.target())
                ;

        //1. 获取刷新注解信息
        CacheInterceptor cacheInterceptor = context.interceptor();
        //执行拦截器
        this.interceptorHandler(cacheInterceptor, interceptorContext, cache, true);

        //2. 执行目标方法
        Object result = context.process();

        //结束时间
        final long endMills = System.currentTimeMillis();
        interceptorContext.endMills(endMills).result(result);

        // 方法执行完成
        this.interceptorHandler(cacheInterceptor, interceptorContext, cache, false);
        return result;
    }

    /**
     * 拦截器执行类
     * 类似AOP增强
     * 反射原理
     * @param cacheInterceptor 缓存拦截器
     * @param interceptorContext 上下文
     * @param cache 缓存
     * @param before 是否执行执行
     */
    @SuppressWarnings("all")
    private void interceptorHandler(CacheInterceptor cacheInterceptor,
                                    CacheInterceptorContext interceptorContext,
                                    ICache cache,
                                    boolean before) {
        if(cacheInterceptor != null) {
            //1. 通用
            if(cacheInterceptor.common()) {
                for(ICacheInterceptor interceptor : commonInterceptors) {
                    if(before) {
                        interceptor.before(interceptorContext);
                    } else {
                        interceptor.after(interceptorContext);
                    }
                }
            }

            //2. 刷新
            if(cacheInterceptor.refresh()) {
                for(ICacheInterceptor interceptor : refreshInterceptors) {
                    if(before) {
                        interceptor.before(interceptorContext);
                    } else {
                        interceptor.after(interceptorContext);
                    }
                }
            }

            //3. AOF 追加
            final ICachePersist cachePersist = cache.persist();
            if(cacheInterceptor.aof() && (cachePersist instanceof CachePersistAof)) {
                if(before) {
                    persistInterceptors.before(interceptorContext);
                } else {
                    persistInterceptors.after(interceptorContext);
                }
            }

            //4. 驱除策略更新
            if(cacheInterceptor.evict()) {
                if(before) {
                    evictInterceptors.before(interceptorContext);
                } else {
                    evictInterceptors.after(interceptorContext);
                }
            }
        }
    }
}
