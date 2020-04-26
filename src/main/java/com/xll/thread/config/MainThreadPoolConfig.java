package com.xll.thread.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @Author：xuliangliang
 * @Description：
 * @Date 2020/4/26
 */
@Component
public class MainThreadPoolConfig {
    /**
     * 核心线程数
     */
    @Value("${com.xll.thread.corePoolSize:50}")
    private Integer poolSize;
    /**
     * 最大线程数
     */
    @Value("${com.xll.thread.maximumPoolSize:100}")
    private Integer maximumPoolSize;

    public MainThreadPoolConfig() {
    }

    public Integer getPoolSize() {
        return this.poolSize;
    }

    public Integer getMaximumPoolSize() {
        return this.maximumPoolSize;
    }

    public Long getKeepAliveTime() {
        return 0L;
    }

    public TimeUnit getUnit() {
        return TimeUnit.MILLISECONDS;
    }

    /**
     * 初始化线程池
     * @return
     */
    @Bean
    @PostConstruct
    public ThreadPoolExecutor initThreadPool() {
        return new ThreadPoolExecutor(this.poolSize, this.poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), Executors.defaultThreadFactory());
    }
}
