package com.xingtao.cache.core.support.expire;

import com.xingtao.cache.api.cacheAPI.ICache;
import com.xingtao.cache.api.cacheAPI.ICacheExpire;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description  缓存过期——优化普通策略
 * 如果过期的应用场景不多，那么经常轮训的意义实际不大。
 *
 * 比如我们的任务 99% 都是在凌晨清空数据，白天无论怎么轮询，纯粹是浪费资源。
 *
 * 那有没有什么方法，可以快速的判断有没有需要处理的过期元素呢？
 *
 * 答案是有的，那就是排序的 MAP。
 *
 * 我们换一种思路，让过期的时间做 key，相同时间的需要过期的信息放在一个列表中，作为 value。
 *
 * 然后对过期时间排序，轮询的时候就可以快速判断出是否有过期的信息了。
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.expire
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/9
 */
public class CacheExpireSort<K,V> implements ICacheExpire<K,V> {
    /**
     * 单次清空的数量限制
     * @since 0.0.3
     */
    private static final int LIMIT = 100;

    /**
     * 排序缓存存储
     *
     * 使用按照时间排序的缓存处理。
     * @since 0.0.3
     */
    private final Map<Long, List<K>> sortMap = new TreeMap<>(new Comparator<Long>() {
        @Override
        public int compare(Long o1, Long o2) {
            return (int) (o1-o2);
        }
    });

    /**
     * 过期 map
     *
     * 空间换时间
     * @since 0.0.3
     */
    private final Map<K, Long> expireMap = new HashMap<>();

    /**
     * 缓存实现
     * @since 0.0.3
     */
    private final ICache<K,V> cache;

    /**
     * 线程执行类
     * @since 0.0.3
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public CacheExpireSort(ICache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    /**
     * 初始化任务
     * @since 0.0.3
     */
    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThread(), 100, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * 定时执行任务
     * @since 0.0.3
     */
    private class ExpireThread implements Runnable {
        @Override
        public void run() {
            //1.判断是否为空
            if(ObjectUtils.isEmpty(sortMap)) {
                return;
            }

            //2. 获取 key 进行处理
            int count = 0;
            for(Map.Entry<Long, List<K>> entry : sortMap.entrySet()) {
                final Long expireAt = entry.getKey();
                List<K> expireKeys = entry.getValue();

                // 判断队列是否为空
                if(ObjectUtils.isEmpty(expireKeys)) {
                    sortMap.remove(expireAt);
                    continue;
                }
                if(count >= LIMIT) {
                    return;
                }

                //todo interator方式遍历和foreach方式遍历，谁的效率更高一点？

                // 删除的逻辑处理
                long currentTime = System.currentTimeMillis();
                if(currentTime >= expireAt) {
                    Iterator<K> iterator = expireKeys.iterator();
                    while (iterator.hasNext()) {
                        K key = iterator.next();
                        // 先移除本身
                        iterator.remove();
                        expireMap.remove(key);

                        // 再移除缓存，后续可以通过惰性删除做补偿
                        cache.remove(key);

                        count++;
                    }
                } else {
                    // 直接跳过，没有过期的信息
                    return;
                }
            }
        }
    }

    /**
     * 放入元素时，同时让如到TreeMap和HashMap中
     * @param key
     * @param expireAt 什么时候过期
     */
    @Override
    public void expire(K key, long expireAt) {
        List<K> keys = sortMap.get(expireAt);
        if(keys == null) {
            keys = new ArrayList<>();
        }
        keys.add(key);

        // 设置对应的信息
        sortMap.put(expireAt, keys);
        expireMap.put(key, expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if(ObjectUtils.isEmpty(keyList)) {
            return;
        }

        // todo 这样维护两套的代价太大，后续优化，暂时不用。内存会翻倍。
        //这里如果没有expireMap，那么内存不会翻倍，但是在惰性删除时，就需要遍历一遍sortMap才能找到需要判断的key。
        // 判断大小，小的作为外循环
        final int expireSize = expireMap.size();
        if(expireSize <= keyList.size()) {
            // 一般过期的数量都是较少的
            for(Map.Entry<K,Long> entry : expireMap.entrySet()) {
                K key = entry.getKey();

                // 这里直接执行过期处理，不再判断是否存在于集合中。
                // 因为基于集合的判断，时间复杂度为 O(n)
                this.removeExpireKey(key);
            }
        } else {
            for(K key : keyList) {
                this.removeExpireKey(key);
            }
        }
    }

    /**
     * 返回数据的过期时间（可能过期，也可能不过期，只是返回时间值）
     * @param key  还没有过期的key
     * @return
     */
    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }

    /**
     * 移除过期信息
     * @param key key
     * @since 0.0.10
     */
    private void removeExpireKey(final K key) {
        Long expireTime = expireMap.get(key);
        if(expireTime != null) {
            final long currentTime = System.currentTimeMillis();
            if(currentTime >= expireTime) {
                expireMap.remove(key);

                List<K> expireKeys = sortMap.get(expireTime);
                expireKeys.remove(key);
                sortMap.put(expireTime, expireKeys);
            }
        }
    }
}
