package com.xingtao.cache.core.support.listener.slow;

import com.alibaba.fastjson.JSON;
import com.xingtao.cache.api.cacheAPI.ICacheSlowListener;
import com.xingtao.cache.api.cacheAPI.ICacheSlowListenerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 慢日志监听类
 * @author xingtao
 */
public class CacheSlowListener implements ICacheSlowListener {

    private static final Logger log = LoggerFactory.getLogger(CacheSlowListener.class);

    @Override
    public void listen(ICacheSlowListenerContext context) {
        log.warn("[Slow] methodName: {}, params: {}, cost time: {}",
                context.methodName(), JSON.toJSON(context.params()), context.costTimeMills());
    }

    @Override
    public long slowerThanMills() {
        return 1000L;
    }

}
