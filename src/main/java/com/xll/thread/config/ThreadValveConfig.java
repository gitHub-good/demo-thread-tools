package com.xll.thread.config;

import com.xll.thread.exception.ThreadToolException;
import com.xll.thread.tool.DynamicSemaphore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @Author：xuliangliang
 * @Description：线程阀值配置类
 * @Date 2020/4/26
 */
public class ThreadValveConfig {

    private static final Logger log = LoggerFactory.getLogger(ThreadValveConfig.class);
    /**
     * 信号量并发hash
     */
    private ConcurrentHashMap<String, DynamicSemaphore> semaphoreConcurrentHashMap = new ConcurrentHashMap();
    /**
     * 计数器 例如："A:10,B:12,C:13" 字符串
     */
    private String counter;
    /**
     * 计数器map容器， 例如：key:A, value:10等
     */
    private ConcurrentHashMap<String, Integer> counterMap = new ConcurrentHashMap();

    public ThreadValveConfig() {
    }

    /**
     * 设置计数器值
     * @param counter
     */
    public void setCounter(String counter) {
        this.counter = counter;
        String[] counterList = counter.split(",");

        for(int i = 0; i < counterList.length; ++i) {
            String valve = counterList[i];
            this.counterMap.put(valve.split(":")[0], Integer.valueOf(valve.split(":")[1]));
        }

    }

    /**
     * 获取计数器Map集合
     * @return
     */
    public ConcurrentHashMap<String, Integer> getCounterMap() {
        return this.counterMap;
    }

    /**
     * 设置计数器map集合
     * @param counterMap
     */
    public void setCounterMap(ConcurrentHashMap<String, Integer> counterMap) {
        this.counterMap = counterMap;
    }

    /**
     * 获取信号量Map集合
     * @return
     */
    public ConcurrentHashMap<String, DynamicSemaphore> getSemaphoreConcurrentHashMap() {
        return this.semaphoreConcurrentHashMap;
    }

    /**
     * 获取线程值
     * @param valve
     * @return
     * @throws InterruptedException
     */
    public synchronized DynamicSemaphore getThreadValve(String valve) throws InterruptedException {
        Integer num = this.counterMap.get(valve);
        if (null != num) {
            if (null != this.semaphoreConcurrentHashMap.get(valve)) {
                (this.semaphoreConcurrentHashMap.get(valve)).setPermits(this.counterMap.get(valve));
                return this.semaphoreConcurrentHashMap.get(valve);
            } else {
                DynamicSemaphore semaphore = new DynamicSemaphore((Integer)this.counterMap.get(valve));
                this.semaphoreConcurrentHashMap.put(valve, semaphore);
                return semaphore;
            }
        } else {
            log.warn("thread model {} not configured！", valve);
            throw new ThreadToolException("model " + valve + " not configured");
        }
    }

    /**
     * 定时清除信号量Map数据，防止数据大内存溢出
     */
    @Scheduled(fixedDelay = 30000L)
    public void cleanSemaphoreConcurrentHashMap() {
        Iterator iterable = this.semaphoreConcurrentHashMap.keySet().iterator();

        while(iterable.hasNext()) {
            String valve = (String)iterable.next();
            if (null != this.counterMap.get(valve)) {
                this.semaphoreConcurrentHashMap.remove(valve);
            }
        }

    }
}
