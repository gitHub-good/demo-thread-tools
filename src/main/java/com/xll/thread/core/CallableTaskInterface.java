package com.xll.thread.core;
/**
 *
 * @Author：xuliangliang
 * @Description：有返回值的线程任务接口
 * @Date 2020/4/26
 */
public interface CallableTaskInterface<T, R> {
    /**
     * 执行任务接口方法
     * @param t
     * @return
     */
    R doTask(T t);
}
