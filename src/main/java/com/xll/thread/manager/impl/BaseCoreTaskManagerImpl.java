package com.xll.thread.manager.impl;

import com.xll.thread.config.ThreadValveConfig;
import com.xll.thread.core.CallableMethodThread;
import com.xll.thread.core.CallableTaskInterface;
import com.xll.thread.core.CommonMethodThread;
import com.xll.thread.core.CommonTaskInterface;
import com.xll.thread.exception.ThreadToolException;
import com.xll.thread.manager.BaseCoreTaskManager;
import com.xll.thread.tool.DynamicSemaphore;
import com.xll.thread.tool.SynchronizationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @Author：xuliangliang
 * @Description：基础核心线程任务执行接口实现
 * @Date 2020/4/26
 */
@Service("baseCoreTaskManager")
public class BaseCoreTaskManagerImpl implements BaseCoreTaskManager {
    private static final Logger log = LoggerFactory.getLogger(BaseCoreTaskManagerImpl.class);
    @Autowired
    private ThreadValveConfig threadValveConfig;
    @Autowired
    private ThreadPoolExecutor mainThreadPool;

    public BaseCoreTaskManagerImpl() {
    }

    @Override
    public <T> void runTaskWithOutCallBack(String modelName, List<T> tasks, CommonTaskInterface<T> commonTaskInterface, Enum type) throws InterruptedException {
        if (null == modelName) {
            modelName = "default";
        }

        DynamicSemaphore semaphore = this.threadValveConfig.getThreadValve(modelName);
        if (null == semaphore) {
            log.error("default thread valve was not config!");
            throw new ThreadToolException("default thread valve was not config!");
        } else if (semaphore.getPermits() < 1) {
            log.error("thread valve was not config!");
            throw new ThreadToolException("thread valve was not config!");
        } else {
            CountDownLatch countDownLatch = new CountDownLatch(tasks.size());
            Iterator iteratorTask = tasks.iterator();

            while(iteratorTask.hasNext()) {
                final T task = (T) iteratorTask.next();
                if (SynchronizationType.SYN.equals(type)) {
                    this.mainThreadPool.execute(new CommonMethodThread(modelName, semaphore, countDownLatch) {
                        @Override
                        public void doTask() {
                            commonTaskInterface.doTask(task);
                        }
                    });
                } else {
                    this.mainThreadPool.execute(new CommonMethodThread(modelName, semaphore) {
                        @Override
                        public void doTask() {
                            commonTaskInterface.doTask(task);
                        }
                    });
                }
            }

            if (SynchronizationType.SYN.equals(type)) {
                countDownLatch.await();
            }

        }
    }

    @Override
    public <R, T> List<Future<R>> runTaskWithCallBack(String modelName, List<T> tasks, CallableTaskInterface<T, R> callableTaskInterface) throws InterruptedException {
        if (null == modelName) {
            modelName = "default";
        }

        DynamicSemaphore semaphore = this.threadValveConfig.getThreadValve(modelName);
        if (null == semaphore) {
            log.error("default thread valve was not config!");
            throw new ThreadToolException("default thread valve was not config!");
        } else {
            Collection<CallableMethodThread<R>> callableMethodThreadList = new ArrayList();
            Iterator iteratorTask = tasks.iterator();

            while(iteratorTask.hasNext()) {
                final T task = (T) iteratorTask.next();
                callableMethodThreadList.add(new CallableMethodThread<R>(modelName, semaphore) {
                    @Override
                    public R doTask() {
                        return callableTaskInterface.doTask(task);
                    }
                });
            }

            return this.mainThreadPool.invokeAll(callableMethodThreadList);
        }
    }

}
