package com.xingtao.cache.core.support.persist;

import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.xingtao.cache.api.cacheAPI.ICache;
import com.xingtao.cache.api.cacheAPI.ICachePersist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @Description Aof持久化
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.persist
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/17
 */
public class CachePersistAof<K,V> implements ICachePersist<K,V> {

    private static final Logger log = LoggerFactory.getLogger(CachePersistAof.class);

    /**
     * 缓存列表
     */
    private final List<String> bufferList = new ArrayList<>();

    /**
     * 数据持久化路径
     */
    private final String dbPath;

    public CachePersistAof(String dbPath) {
        this.dbPath = dbPath;
    }


    @Override
    public void persist(ICache<K, V> cache) {
        log.info("开始 AOF 持久化到文件");
        // 1. 创建文件
        if(!FileUtil.exists(dbPath)) {
            FileUtil.createFile(dbPath);
        }
        // 2. 持久化追加到文件中
        FileUtil.append(dbPath, bufferList);

        // 3. 清空 buffer 列表
        bufferList.clear();
        log.info("完成 AOF 持久化到文件");
    }

    /**
     * 添加文件内容到 buffer 列表中
     * @param json json 信息
     */
    public void append(final String json) {
        if(StringUtil.isNotEmpty(json)) {
            bufferList.add(json);
        }
    }

    @Override
    public long delay() {
        return 1;
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
