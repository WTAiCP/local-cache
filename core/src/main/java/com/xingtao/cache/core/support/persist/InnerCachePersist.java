package com.xingtao.cache.core.support.persist;


import com.xingtao.cache.api.cacheAPI.ICache;
import com.xingtao.cache.api.cacheAPI.ICachePersist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 内部缓存持久化类
 * @author WT
 * @param <K> key
 * @param <V> value
 */
public class InnerCachePersist<K,V> {

    private static final Logger log = LoggerFactory.getLogger(InnerCachePersist.class);

    /**
     * 缓存信息
     */
    private final ICache<K,V> cache;

    /**
     * 缓存持久化策略
     */
    private final ICachePersist<K,V> persist;

    /**
     * 线程执行类
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public InnerCachePersist(ICache<K, V> cache, ICachePersist<K, V> persist) {
        this.cache = cache;
        this.persist = persist;

        // 初始化
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("开始持久化缓存信息");
                    persist.persist(cache);
                    log.info("完成持久化缓存信息");
                } catch (Exception exception) {
                    log.error("文件持久化异常", exception);
                }
            }
        }, persist.delay(), persist.period(), persist.timeUnit());
    }

}
