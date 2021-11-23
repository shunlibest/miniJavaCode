package com.线程池;

import java.util.concurrent.*;

public class ExecutorsMini {


    /**
     * 创建一个核心线程数和最大线程数都是1
     */
    public static ThreadPoolExecutor newSingleThreadExecutor() {
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
    }


    /**
     * 创建一个线程池，该线程池会有最多N个固定数量的线程。
     * N ：核心线程数 & 最大线程数
     */
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }


}
