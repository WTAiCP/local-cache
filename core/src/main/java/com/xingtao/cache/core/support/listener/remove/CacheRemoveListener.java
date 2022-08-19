package com.xingtao.cache.core.support.listener.remove;

import com.xingtao.cache.api.cacheAPI.ICacheRemoveListener;
import com.xingtao.cache.api.cacheAPI.ICacheRemoveListenerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description  默认删除监听类
 * @Version
 * @BelongsPackage com.xingtao.cache.core.support.listener.remove
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/12
 */
public class CacheRemoveListener<K,V> implements ICacheRemoveListener<K,V> {

    private static final Logger log = LoggerFactory.getLogger(CacheRemoveListener.class);

    @Override
    public void listen(ICacheRemoveListenerContext<K, V> context) {
        log.debug("Remove key: {}, value: {}, type: {}",
                context.key(),context.value(),context.type());
    }
}
