package com.xingtao.cache.test;

import com.xingtao.cache.api.cacheAPI.ICache;
import com.xingtao.cache.core.bs.CacheBs;

/**
 * @Description API调用测试
 * 测试功能是否完整
 * @Version
 * @BelongsPackage com.xingtao.test.test
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/19
 */
public class MyCache {
    public static void main(String[] args) {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(2)
                .build();

        cache.put("1", "11");
        cache.put("2", "22");
        cache.put("3", "33");
        cache.put("4", "44");
        cache.put("5", "55");

    }
}
