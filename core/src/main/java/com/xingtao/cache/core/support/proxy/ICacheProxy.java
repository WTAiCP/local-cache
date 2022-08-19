package com.xingtao.cache.core.support.proxy;

/**
 * @Description 缓存代理接口
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.proxy
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/12
 */
public interface ICacheProxy {

    /**
     * 获取代理实现
     * @return
     */
    Object proxy();
}
