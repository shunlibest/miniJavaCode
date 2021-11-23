package com.线程池;


import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static com.线程池.ThreadPoolInfoModel.*;

public class ThreadPoolExecutorMini {

    /**
     * 用一个整数，控制线程池的状态；而线程池的状态，由两个信息拼在一起；分别是
     * 线程状态：前3位
     * 工作线程数量：后29位
     */
    private final ThreadPoolInfoModel threadPoolInfo = new ThreadPoolInfoModel();

    private final ThreadPollParameter pollParameter = new ThreadPollParameter();


    //包含池中所有工作线程的集合
    private final HashSet<Worker> workers = new HashSet<Worker>();
    //跟踪达到的最大池大小。
    private int largestPoolSize;


    /**
     * 首先，看最最重要的一个方法
     * 向线程池中添加任务，并按照一定的规则执行；
     */
    public void execute(@NotNull Runnable task) {
        /*
         * 主要过程有三步:
         * Step1: 如果正在运行的线程少于corePoolSize，那么将会创建一个新的线程
         * Step2. 如何线程数量大于corePoolSize，那么就开始排队了
         * Step3. 当队列满了之后，就创建新的线程，不过不能超过最大的线程数量
         */
        int state = threadPoolInfo.get();
        if (ThreadPoolInfoModel.getWorkerCount(state) < pollParameter.getCorePoolSize()) {
            if (addWorker(task, true))
                return;
            state = threadPoolInfo.get();
        }
        if (isRunning(c) && workQueue.offer(task)) {
            int recheck = ctl.get();
            if (!isRunning(recheck) && remove(task))
                reject(task);
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        } else if (!addWorker(task, false))
            reject(command);
    }


    /**
     * 检查是否可以根据当前池状态和给定界限（核心或最大值）添加新的工作线程。
     * 如果是这样，则相应地调整工作人员数量，并且如果可能，将创建并启动一个新工作人员，将 firstTask 作为其第一个任务运行。
     * 如果池已停止或有资格关闭，则此方法返回 false。 如果线程工厂在询问时未能创建线程，它也会返回 false。
     * 如果线程创建失败，要么是由于线程工厂返回 null，要么是由于异常（通常是 Thread.start() 中的 OutOfMemoryError），
     */
    private boolean addWorker(Runnable firstTask, boolean core) {
        retry:
        for (; ; ) {
            int info = threadPoolInfo.get();
            int runState = getRunState(info);

            // 判断一些不能添加到工作队列的条件
            if (runState >= SHUTDOWN && !(runState == SHUTDOWN && firstTask == null && !workQueue.isEmpty())) {
                return false;
            }

            for (; ; ) {
                int wc = getWorkerCount(info);
                if (wc >= CAPACITY ||
                        wc >= (core ? pollParameter.getCorePoolSize() : pollParameter.getMaximumPoolSize())) {
                    return false;
                }

                if (threadPoolInfo.compareAndIncrementWorkerCount(info))
                    break retry;
                info = threadPoolInfo.get();  // Re-read ctl
                if (ThreadPoolInfoModel.getRunState(info) != runState)
                    continue retry;
                // else CAS failed due to workerCount change; retry inner loop
            }
        }

        boolean workerStarted = false;
        boolean workerAdded = false;
        Worker w = null;
        try {
            w = new Worker(firstTask);
            final Thread t = w.thread;
            if (t != null) {
                final ReentrantLock mainLock = this.mainLock;
                mainLock.lock();
                try {
                    // Recheck while holding lock.
                    // Back out on ThreadFactory failure or if
                    // shut down before lock acquired.
                    int rs = getRunState(threadPoolInfo.get());

                    if (rs < SHUTDOWN || (rs == SHUTDOWN && firstTask == null)) {
                        if (t.isAlive()) // precheck that t is startable
                            throw new IllegalThreadStateException();
                        workers.add(w);
                        largestPoolSize = Math.max(largestPoolSize, workers.size());
                        workerAdded = true;
                    }
                } finally {
                    mainLock.unlock();
                }
                if (workerAdded) {
                    t.start();
                    workerStarted = true;
                }
            }
        } finally {
            if (!workerStarted)
                addWorkerFailed(w);
        }
        return workerStarted;
    }


}
