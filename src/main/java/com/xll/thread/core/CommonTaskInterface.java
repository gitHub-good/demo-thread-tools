package com.xll.thread.core;

/**
 *
 * @Author：xuliangliang
 * @Description：通用任务接口
 * @Date 2020/4/26
 */
public interface CommonTaskInterface<T> {
    /**
     * 执行任务接口方法
     * @param params
     */
    void doTask(T params);
}
