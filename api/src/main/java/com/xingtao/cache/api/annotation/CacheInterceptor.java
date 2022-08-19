package com.xingtao.cache.api.annotation;

import java.lang.annotation.*;

/**
 * @Description 缓存拦截器
 *   自定义注解
 * @Version
 * @BelongsPackage com.xingtao.cache.api.annotation
 * @BelongsProject local-cache-based-on-Java
 * @Author WT
 * @Date 2022/2/12
 *
 * Target描述了注解修饰的对象范围.
     * METHOD：用于描述方法
     * PACKAGE：用于描述包
     * PARAMETER：用于描述方法变量
     * TYPE：用于描述类、接口或enum类型
 * Retention表示注解保留时间长短
     * SOURCE：在源文件中有效，编译过程中会被忽略
     * CLASS：随源文件一起编译在class文件中，运行时忽略
     * RUNTIME：在运行时有效
 * Documented Inherited为元注解，修饰注解
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheInterceptor {

    /**
     * 操作是否需要 append to file，默认为 false
     * 主要针对 cache 内容有变更的操作，不包括查询操作。
     * 包括删除，添加，过期等操作。
     * @return
     */
    boolean aof() default false;

    /**
     * 通用拦截器
     *
     * 1. 耗时统计
     * 2. 慢日志统计
     *
     * etc.
     * @return 默认开启
     */
    boolean common() default true;

    /**
     * 是否启用刷新
     * @return false
     * @since 0.0.5
     */
    boolean refresh() default false;

    /**
     * 是否执行驱除更新
     *
     * 主要用于 LRU/LFU 等驱除策略
     * @return 是否
     * @since 0.0.11
     */
    boolean evict() default false;

}
