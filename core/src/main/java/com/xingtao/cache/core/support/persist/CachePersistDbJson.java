package com.xingtao.cache.core.support.persist;

import com.alibaba.fastjson.JSON;
import com.xingtao.cache.api.cacheAPI.ICache;
import com.xingtao.cache.api.cacheAPI.ICachePersist;
import com.xingtao.cache.core.model.PersistRdbEntry;
import com.xingtao.cache.core.utils.FileUtil;

import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description RDB方式：转为JSON数据
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.load
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/11
 */
public class CachePersistDbJson<K,V> implements ICachePersist<K,V> {

    /**
     * 数据库路径
     */
    private final String dbPath;

    public CachePersistDbJson(String dbPath) {
        this.dbPath = dbPath;
    }

    /**
     * 持久化
     * key长度 key+value
     * 第一个空格，获取 key 的长度，然后截取
     * @param cache
     */
    @Override
    public void persist(ICache<K, V> cache) {
         Set<Map.Entry<K,V>> entrySet = cache.entrySet();

        // 创建文件
        FileUtil.createFile(dbPath);
        // 清空文件
        FileUtil.truncate(dbPath);

        for (Map.Entry<K,V> entry : entrySet) {
            K key = entry.getKey();
            //获取过期时间，若没有则返回null
            Long expireTime = cache.expire().expireTime(key);
            PersistRdbEntry<K,V> persistRdbEntry = new PersistRdbEntry<>();
            persistRdbEntry.setKey(key);
            persistRdbEntry.setValue(entry.getValue());
            persistRdbEntry.setExpire(expireTime);

            String line = JSON.toJSONString(persistRdbEntry);
            FileUtil.write(dbPath, line, StandardOpenOption.APPEND);
        }
    }

    @Override
    public long delay() {
        return 5;
    }

    @Override
    public long period() {
        return 5;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.MINUTES;
    }

}
