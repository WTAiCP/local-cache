package com.xingtao.cache.core.core;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.xingtao.cache.api.annotation.CacheInterceptor;
import com.xingtao.cache.api.cacheAPI.*;
import com.xingtao.cache.core.constant.enums.CacheRemoveType;
import com.xingtao.cache.core.exception.CacheRuntimeException;
import com.xingtao.cache.core.support.evict.CacheEvictContext;
import com.xingtao.cache.core.support.expire.CacheExpire;
import com.xingtao.cache.core.support.listener.remove.CacheRemoveListenerContext;
import com.xingtao.cache.core.support.proxy.CacheProxy;

import java.util.*;

/**
 * @Description
 *  1、针对必须要实现的Map方法，重写了Map的put、get方法，剩余的均使用原生Map方法。
 *  2、实现ICache接口的方法。
 *
 *      1、新增sizeLimit方法，设置尺寸大小
 * @Version
 * @BelongsPackage com.xingtao.cache.core.core
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/9
 */
public class Cache<K,V> implements ICache<K,V> {

    private Map<K,V> map;

    /**
     * 限制大小
     */
    private int sizeLimit;

    /**
     * 过期策略
     */
    private ICacheExpire<K,V> cacheExpire;

    /**
     * 删除监听类
     */
    private List<ICacheRemoveListener<K,V>> removeListeners;

    /**
     * 慢日志监听类
     */
    private List<ICacheSlowListener> slowListeners;

    /**
     * 加载类
     */
    private ICacheLoad<K,V> load;

    /**
     * 持久化
     */
    private ICachePersist<K,V> persist;

    private ICacheEvict<K,V> evict;

    /**
     * 获取驱除策略
     * @return
     */
    @Override
    public ICacheEvict<K, V> evict() {
        return this.evict;
    }

    /**
     * 设置驱除策略
     * @param cacheEvict 驱除策略
     * @return
     */
    public Cache<K, V> evict(ICacheEvict<K, V> cacheEvict) {
        this.evict = cacheEvict;
        return this;
    }

    /**
     * 实现map
     * @param map
     * @return
     */
    public Cache<K, V> map(Map<K,V> map) {
        this.map = map;
        return this;
    }

    /**
     * 设置大小
     * @param sizeLimit
     * @return
     */
    public Cache<K,V> sizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
        return this;
    }



    @Override
    public ICacheExpire<K, V> expire() {
        return this.cacheExpire;
    }

    @Override
    public List<ICacheRemoveListener<K, V>> removeListeners() {

        return removeListeners;
    }

    public Cache<K, V> removeListeners(List<ICacheRemoveListener<K, V>> removeListeners) {
        this.removeListeners = removeListeners;
        return this;
    }

    @Override
    public List<ICacheSlowListener> slowListeners() {
        return slowListeners;
    }

    public Cache<K, V> slowListeners(List<ICacheSlowListener> slowListeners) {
        this.slowListeners = slowListeners;
        return this;
    }

    @Override
    public ICacheLoad<K, V> load() {
        return load;
    }

    public Cache<K, V> load(ICacheLoad<K, V> load) {
        this.load = load;
        return this;
    }

    @Override
    public ICachePersist<K, V> persist() {
        return persist;
    }

    /**
     * 设置持久化策略
     * @param persist 持久化
     */
    public void persist(ICachePersist<K, V> persist) {
        this.persist = persist;
    }

    /**
     * 初始化
     */
    public void init() {
        this.cacheExpire = new CacheExpire<>(this);
        /*this.load.load(this);*/

        /*// 初始化持久化
        if(this.persist != null) {
            new InnerCachePersist<>(this, persist);
        }*/
    }

    /**
     * 实现ICache接口
     * @param key
     * @param timeInMills 毫秒
     * @return
     */
    @Override
    @CacheInterceptor
    public ICache<K, V> expire(K key, long timeInMills) {
        long expireTime = System.currentTimeMillis() + timeInMills;

        // 动态代理调用
        Cache<K,V> cacheProxy = (Cache<K, V>) CacheProxy.getProxy(this);
        return cacheProxy.expireAt(key,expireTime);

    }

    /**
     * 实现ICache接口中
     * 在指定时间过期
     * @param key
     * @param timeInMills
     * @return
     */
    @Override
    @CacheInterceptor(aof = true)
    public ICache<K, V> expireAt(K key, long timeInMills) {
        this.cacheExpire.expire(key,timeInMills);
        return this;
    }

    /**
     * ICache继承Map，必须要实现的
     * @return
     */
    @Override
    @CacheInterceptor(refresh = true)
    public int size() {
        return map.size();
    }

    /**
     * ICache继承Map，必须要实现的
     * @return
     */
    @Override
    @CacheInterceptor(refresh = true)
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * ICache继承Map，必须要实现的
     * @return
     */
    @Override
    @CacheInterceptor(refresh = true,evict = true)
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    /**
     * ICache继承Map，必须要实现的
     * @return
     */
    @Override
    @CacheInterceptor(refresh = true)
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    /**
     * ICache继承Map，必须要实现的
     * @return
     */
    @Override
    @CacheInterceptor(evict = true)
    public V get(Object key) {
        K genericKey = (K) key;
        this.cacheExpire.refreshExpire(Collections.singletonList(genericKey));

        return  map.get(key);
    }

    /**
     * ICache继承Map，必须要实现的
     * @return
     */
    @Override
    @CacheInterceptor(aof = true,evict = true)
    public V put(K key, V value) {
        //1.1 尝试驱除
        CacheEvictContext<K,V> context = new CacheEvictContext<>();
        context.key(key).size(sizeLimit).cache(this);

        ICacheEntry<K,V> evictEntry = evict.evict(context);

        // 添加拦截器调用
        if(ObjectUtil.isNotNull(evictEntry)) {
            // 执行淘汰监听器
            ICacheRemoveListenerContext<K,V> removeListenerContext = CacheRemoveListenerContext.<K,V>newInstance().key(evictEntry.key())
                    .value(evictEntry.value())
                    .type(CacheRemoveType.EVICT.code());
            for(ICacheRemoveListener<K,V> listener : context.cache().removeListeners()) {
                listener.listen(removeListenerContext);
            }
        }

        //2. 判断驱除后的信息
        if(isSizeLimit()) {
            throw new CacheRuntimeException("当前队列已满，数据添加失败！");
        }

        //3. 执行添加
        return map.put(key, value);
    }

    /**
     * 是否已经达到大小最大的限制
     * @return 是否限制
     */
    private boolean isSizeLimit() {
        final int currentSize = this.size();
        return currentSize >= this.sizeLimit;
    }

    /**
     * ICache继承Map，必须要实现的
     * @return
     */
    @Override
    @CacheInterceptor(aof = true, evict = true)
    public V remove(Object key) {
        return map.remove(key);
    }

    /**
     * ICache继承Map，必须要实现的
     * @return
     */
    @Override
    @CacheInterceptor(aof = true)
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    /**
     * ICache继承Map，必须要实现的
     * @return
     */
    @Override
    @CacheInterceptor(aof = true, refresh = true)
    public void clear() {
        map.clear();
    }

    /**
     * ICache继承Map，必须要实现的
     * @return
     */
    @Override
    @CacheInterceptor(refresh = true)
    public Set<K> keySet() {
        return map.keySet();
    }

    /**
     * ICache继承Map，必须要实现的
     * @return
     */
    @Override
    @CacheInterceptor(refresh = true)
    public Collection<V> values() {
        return map.values();
    }

    /**
     * ICache继承Map，必须要实现的
     * @return
     */
    @Override
    @CacheInterceptor(refresh = true)
    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

}
