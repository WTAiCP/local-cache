package com.xingtao.cache.api.cacheAPI;

import java.util.Map;

/**
 * 缓存上下文
 * @author WT
 */
public interface ICacheContext<K, V> {

    /**
     * map 信息
     * @return map
     */
    Map<K, V> map();

    /**
     * 大小限制
     * @return 大小限制
     */
    int size();

    /**
     * 驱除策略
     * @return 策略
     */
    ICacheEvict<K,V> cacheEvict();

}
