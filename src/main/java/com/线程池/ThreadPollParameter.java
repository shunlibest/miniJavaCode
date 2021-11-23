package com.线程池;

import java.util.concurrent.RejectedExecutionHandler;

//线程池参数
public class ThreadPollParameter {

    private volatile int corePoolSize;      //核心池大小（不允许超时等）
    private volatile int maximumPoolSize;  //最大线程数

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }


    //    /**
//     * Handler called when saturated or shutdown in execute.
//     */
//    private volatile RejectedExecutionHandler handler;
//
//    /**
//     * Timeout in nanoseconds for idle threads waiting for work.
//     * Threads use this timeout when there are more than corePoolSize
//     * present or if allowCoreThreadTimeOut. Otherwise they wait
//     * forever for new work.
//     */
//    private volatile long keepAliveTime;
//
//    /**
//     * If false (default), core threads stay alive even when idle.
//     * If true, core threads use keepAliveTime to time out waiting
//     * for work.
//     */
//    private volatile boolean allowCoreThreadTimeOut;


}
