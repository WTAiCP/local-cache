package com.xingtao.cache.core.support.expire;

import com.xingtao.cache.api.cacheAPI.ICache;
import com.xingtao.cache.api.cacheAPI.ICacheExpire;
import com.xingtao.cache.api.cacheAPI.ICacheRemoveListener;
import com.xingtao.cache.api.cacheAPI.ICacheRemoveListenerContext;
import com.xingtao.cache.core.constant.enums.CacheRemoveType;
import com.xingtao.cache.core.support.listener.remove.CacheRemoveListenerContext;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description 缓存过期——普通策略
 *  1、定义一个hashmap，key是要过期的时间，value是过期时间
 *  2、开启定时任务，100ms清理一次，每次最多100个
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.expire
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/9
 */
public class CacheExpire<K,V> implements ICacheExpire<K,V> {

    /**
     * 单次清空的数量限制
     */
    private static final int LIMIT = 100;

    /**
     * hashmap存放过期数据
     */
    private final Map<K, Long> expireMap = new HashMap<>();

    /**
     * 缓存实现
     */
    private final ICache<K,V> cache;

    /**
     * 线程执行类
     * 用Executors.newSingleThreadScheduledExecutor()实现一个定时任务
     * 线程池
     * @since 0.0.3
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public CacheExpire(ICache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    /**
     * 初始化任务
     * new ExpireThread() 创建一个定时任务，用来清空任务
     * scheduleAtFixedRate（task(Runnable)，delay，period）
     * delay表示执行任务的时间；period表示执行一次任务间隔的时间
     * @since 0.0.3
     */
    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThread(), 100, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * 定时任务
     * 遍历过期数据，判断对应的时间
     * 如果到期则执行清空操作
     * 为了避免单次执行时间过长，最多处理100条
     */
    private class ExpireThread implements Runnable {

        @Override
        public void run() {
            //1、判断是否有已过期的数据
            if (ObjectUtils.isEmpty(expireMap)) {
                return;
            }

            //2、获取key
            int count = 0;
            for (Map.Entry<K,Long> entry : expireMap.entrySet()) {
                if (count >= LIMIT) {
                    return;
                }
                expireKey(entry.getKey(),entry.getValue());
                count++;
            }
        }
    }

    /**
     * 向map(存储有过期时间的数据)放入元素
     * @param key
     * @param expireAt 什么时候过期
     */
    @Override
    public void expire(K key, long expireAt) {
        expireMap.put(key,expireAt);
    }

    /**
     * 惰性删除
     * @param keyList
     */
    @Override
    public void refreshExpire(Collection<K> keyList) {
        if(ObjectUtils.isEmpty(keyList)) {
            return;
        }

        // 判断大小，小的作为外循环。一般都是过期的 keys 比较小。
        if(keyList.size() <= expireMap.size()) {
            for(K key : keyList) {
                Long expireAt = expireMap.get(key);
                expireKey(key, expireAt);
            }
        } else {
            for(Map.Entry<K, Long> entry : expireMap.entrySet()) {
                this.expireKey(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }

    /**
     * 处理过期数据
     * @param key
     * @param expireAt
     */
    public void expireKey(final K key, final Long expireAt) {
        if(expireAt == null) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if(currentTime >= expireAt) {
            expireMap.remove(key);
            // 再移除缓存，后续可以通过惰性删除做补偿
            V removeValue = cache.remove(key);

            //执行淘汰监听器
            ICacheRemoveListenerContext<K,V> removeListenerContext = CacheRemoveListenerContext.<K,V>newInstance().key(key).value(removeValue).type(CacheRemoveType.EXPIRE.code());
            for(ICacheRemoveListener<K,V> listener : cache.removeListeners()) {
                listener.listen(removeListenerContext);
            }
        }
    }
}
