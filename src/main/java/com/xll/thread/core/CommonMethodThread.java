package com.xll.thread.core;

import com.xll.thread.tool.DynamicSemaphore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
/**
 *
 * @Author：xuliangliang
 * @Description：抽象通用线程方法
 * @Date 2020/4/26
 */
public abstract class CommonMethodThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(CommonMethodThread.class);
    public String modelName;
    private DynamicSemaphore semaphore;
    private CountDownLatch countDownLatch;

    public CommonMethodThread(String modelName, DynamicSemaphore semaphore, CountDownLatch countDownLatch) {
        this.modelName = modelName;
        this.semaphore = semaphore;
        this.countDownLatch = countDownLatch;
    }

    public CommonMethodThread(String modelName, DynamicSemaphore semaphore) {
        this.modelName = modelName;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            //获取信号量
            this.semaphore.acquire();
            //设置线程名称
            Thread.currentThread().setName(this.modelName);
            //执行任务
            this.doTask();
        } catch (InterruptedException var5) {
            log.error("{} is Interrupted!", this.modelName);
            Thread.currentThread().interrupt();
        } finally {
            if (null != this.countDownLatch) {
                this.countDownLatch.countDown();
            }

            this.semaphore.release();
        }

    }

    public abstract void doTask();
}
