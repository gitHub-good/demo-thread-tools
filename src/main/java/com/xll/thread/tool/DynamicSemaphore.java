package com.xll.thread.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;
/**
 *
 * @Author：xuliangliang
 * @Description：动态信号量类
 * @Date 2020/4/26
 */
public class DynamicSemaphore {
    private static final Logger log = LoggerFactory.getLogger(DynamicSemaphore.class);
    /**
     * 信号量对象
     */
    private DynamicSemaphore.CustomSemaphore semaphore;
    /**
     * 最大信号量许可数
     */
    private int maxPermits;

    public DynamicSemaphore(int permits) {
        this.maxPermits = permits;
        this.semaphore = new DynamicSemaphore.CustomSemaphore(permits);
    }

    public void acquire() throws InterruptedException {
        this.semaphore.acquire();
    }

    /**
     * 获取可用许可数
     * @return
     */
    public int getPermits() {
        return this.semaphore.availablePermits();
    }

    /**
     * 释放信号量
     */
    public void release() {
        this.semaphore.release();
    }

    /**
     * 重新设置信号许可数最大值
     * @param newMax
     */
    public synchronized void setPermits(int newMax) {
        if (newMax < 1) {
            throw new IllegalArgumentException("Semaphore size must be at least 1, was " + newMax);
        } else {
            int delta = newMax - this.maxPermits;
            if (delta != 0) {
                log.info("setPermits maxPermits:{} newMax:{}", this.maxPermits, newMax);
            }

            if (delta > 0) {
                this.semaphore.release(delta);
            } else {
                delta *= -1;
                //减少信号量许可数delta
                this.semaphore.reducePermits(delta);
            }

            this.maxPermits = newMax;
        }
    }

    /**
     * 自定义信号量私有类
     */
    private static final class CustomSemaphore extends Semaphore {
        CustomSemaphore(int permits) {
            super(permits);
        }

        @Override
        protected void reducePermits(int size) {
            super.reducePermits(size);
        }
    }
}
