package com.线程池;

public class MyTask implements Runnable {
    private String taskName;

    public MyTask(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        System.out.println("currentThreadName:"+Thread.currentThread().getName());
    }
}
