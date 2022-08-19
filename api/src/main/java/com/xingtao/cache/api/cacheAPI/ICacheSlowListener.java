package com.xingtao.cache.api.cacheAPI;

/**
 * 慢日志操作接口
 *
 * @author WT
 * @since 0.0.9
 */
public interface ICacheSlowListener {

    /**
     * 监听
     * @param context 上下文
     */
    void listen(final ICacheSlowListenerContext context);

    /**
     * 慢日志的阈值
     * @return 慢日志的阈值
     */
    long slowerThanMills();

}
