package com.xll.thread.manager;

import java.util.concurrent.RejectedExecutionHandler;

/**
 *
 * @Author：xuliangliang
 * @Description：线程池管理接口
 * @Date 2020/4/26
 */
public interface ThreadPoolManger {

    /**
     * 调整修改线程池核心数量和最大数量
     * @param corePoolSize
     * @param maximumPoolSize
     */
    void adjustmentThreadPool(Integer corePoolSize, Integer maximumPoolSize);

    /**
     * 获取线程池堵塞队列数据条数
     * @return
     */
    Integer getBlockQueueNum();

    /**
     * 设置线程池拒绝策略
     * @param rejectedExecutionHandler
     */
    void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler);

    /**
     * 检查线程池阀值
     * @return
     */
    boolean checkThreadValveNum();
}
