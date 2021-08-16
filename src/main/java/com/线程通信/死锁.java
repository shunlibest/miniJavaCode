package com.线程通信;

public class 死锁 {
    static Object 锁A = new Object();
    static Object 锁B = new Object();

    static Thread 小张 = new Thread(() -> {
        synchronized (锁A) {
            System.out.println("小张拿到A锁");
            for (int i = 0; i < 1000; i++) {
            }
            synchronized (锁B) {
                System.out.println("小张完成工作了");
            }
        }
    }, "小张");

    static Thread 老李 = new Thread(() -> {
        synchronized (锁B) {
            System.out.println("老李拿B锁");
            for (int i = 0; i < 1000; i++) {
            }
            synchronized (锁A) {
                System.out.println("老李完成工作了");
            }
        }
    }, "老李");

    static volatile int num = 2;

    public static void main(String[] args) throws InterruptedException {
        小张.start();
        老李.start();
    }


}
