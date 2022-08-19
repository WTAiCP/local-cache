package com.xingtao.cache.core.support.interceptor;

import com.xingtao.cache.api.cacheAPI.ICacheInterceptor;
import com.xingtao.cache.core.support.interceptor.aof.CacheInterceptorAof;
import com.xingtao.cache.core.support.interceptor.common.CacheInterceptorCost;
import com.xingtao.cache.core.support.interceptor.evict.CacheInterceptorEvict;
import com.xingtao.cache.core.support.interceptor.refresh.CacheInterceptorRefresh;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 缓存拦截器工具类
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.interceptor
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/16
 */
public class CacheInterceptors {
    /**
     * 默认通用
     * @return 结果
     */
    @SuppressWarnings("all")
    public static List<ICacheInterceptor> defaultCommonList() {
        List<ICacheInterceptor> list = new ArrayList<>();
        list.add(new CacheInterceptorCost());
        return list;
    }


    /**
     * 默认刷新
     * @return 结果
     */
    @SuppressWarnings("all")
    public static List<ICacheInterceptor> defaultRefreshList() {
        List<ICacheInterceptor> list = new ArrayList<>();
        list.add(new CacheInterceptorRefresh());
        return list;
    }

    /**
     * AOF 模式
     * @return 结果
     */
    @SuppressWarnings("all")
    public static ICacheInterceptor aof() {
        return new CacheInterceptorAof();
    }

    /**
     * 驱除策略拦截器
     * @return 结果
     */
    @SuppressWarnings("all")
    public static ICacheInterceptor evict() {
        return new CacheInterceptorEvict();
    }
}
