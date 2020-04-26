package com.xll.thread.manager.impl;

import com.xll.thread.config.ThreadValveConfig;
import com.xll.thread.manager.ThreadPoolManger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
/**
 *
 * @Author：xuliangliang
 * @Description：
 * @Date 2020/4/26
 */
@Service("threadPoolManger")
public class ThreadPoolMangerImpl implements ThreadPoolManger {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolMangerImpl.class);
    @Autowired(required = false)
    private ThreadPoolExecutor mainThreadPool;
    @Autowired
    private ThreadValveConfig threadValveConfig;

    @Override
    public void adjustmentThreadPool(Integer poolSize, Integer maximumPoolSize) {
        this.mainThreadPool.setCorePoolSize(poolSize);
        this.mainThreadPool.setMaximumPoolSize(poolSize);
    }

    @Override
    public Integer getBlockQueueNum() {
        return this.mainThreadPool.getQueue().size();
    }

    @Override
    public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
        this.mainThreadPool.setRejectedExecutionHandler(rejectedExecutionHandler);
    }

    @Override
    public boolean checkThreadValveNum() {
        int occupy = this.threadValveConfig.getCounterMap().values().stream().mapToInt((i) -> {
            return i;
        }).sum();
        return this.mainThreadPool.getMaximumPoolSize() >= occupy;
    }
}
