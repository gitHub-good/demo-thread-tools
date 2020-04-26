package com.xll.thread.core;

import com.xll.thread.tool.DynamicSemaphore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
/**
 *
 * @Author：xuliangliang
 * @Description：有返回值的抽象接口类
 * @Date 2020/4/26
 */
public abstract class CallableMethodThread<T> implements Callable<T> {
    private static final Logger log = LoggerFactory.getLogger(CallableMethodThread.class);
    /**
     * 设置线程名称
     */
    private String modelName;
    /**
     * 动态信号量
     */
    private DynamicSemaphore semaphore;

    public CallableMethodThread(String modelName, DynamicSemaphore semaphore) {
        this.modelName = modelName;
        this.semaphore = semaphore;
    }
    @Override
    public T call() {
        Object t = null;

        try {
            //占有一个信号量许可
            this.semaphore.acquire();
            //设置线程名称
            Thread.currentThread().setName(this.modelName);
            //执行任务
            t = this.doTask();
        } catch (InterruptedException var6) {
            log.error("{} is Interrupted!", this.modelName);
            Thread.currentThread().interrupt();
        } finally {
            //释放当前信号量许可
            this.semaphore.release();
        }

        return (T) t;
    }

    /**
     * 执行任务方法
     * @return
     */
    public abstract T doTask();
}
