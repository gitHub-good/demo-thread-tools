package com.xll.thread.manager;

import com.xll.thread.core.CallableTaskInterface;
import com.xll.thread.core.CommonTaskInterface;

import java.util.List;
import java.util.concurrent.Future;

/**
 *
 * @Author：xuliangliang
 * @Description：基础核心线程任务执行接口
 * @Date 2020/4/26
 */
public interface BaseCoreTaskManager {

    /**
     * 线程执行任务不需要返回数据调用
     * @param modelName 线程名称
     * @param tasks 任务集合
     * @param commonTaskInterface 真正执行任务的接口实现
     * @param type 同步还是异步枚举
     * @param <T>
     * @throws InterruptedException
     */
    <T> void runTaskWithOutCallBack(String modelName, List<T> tasks, CommonTaskInterface<T> commonTaskInterface, Enum type) throws InterruptedException;

    /**
     * 线程执行任务需要返回数据调用
     * @param modelName 线程名称
     * @param tasks 任务集合
     * @param callableTaskInterface 真正执行任务的接口实现
     * @param <R>
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    <R, T> List<Future<R>> runTaskWithCallBack(String modelName, List<T> tasks, CallableTaskInterface<T, R> callableTaskInterface) throws InterruptedException;
}
