package com.线程池;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolInfoModel {

    private final AtomicInteger threadPoolInfo =
            new AtomicInteger(ThreadPoolInfoModel.ctlOf(ThreadPoolInfoModel.RUNNING, 0));


    private static final int COUNT_BITS = Integer.SIZE - 3;      // 29
    public static final  int CAPACITY   = (1 << COUNT_BITS) - 1; // 00011111 ... ... 11111111

    // 状态在高位存储
    public static final int RUNNING    = -1 << COUNT_BITS;      // 11100000 ... ... 00000000
    public static final int SHUTDOWN   = 0 << COUNT_BITS;      // 00000000 ... ... 00000000
    public static final int STOP       = 1 << COUNT_BITS;      // 00100000 ... ... 00000000
    public static final int TIDYING    = 2 << COUNT_BITS;      // 01000000 ... ... 00000000
    public static final int TERMINATED = 3 << COUNT_BITS;      // 01100000 ... ... 00000000


    public int get(){
        return threadPoolInfo.get();
    }

    public static int ctlOf(int rs, int wc) {
        return rs | wc;
    }

    // Packing and unpacking ctl
    public static int getRunState(int c) {
        return c & ~CAPACITY;
    }



    public static int getWorkerCount(int c) {
        return c & CAPACITY;
    }

    public boolean compareAndIncrementWorkerCount(int expect) {
        return threadPoolInfo.compareAndSet(expect, expect + 1);
    }

}
