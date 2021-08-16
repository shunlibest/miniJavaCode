package com.线程通信;

public class 红绿灯死锁 {
    static Object 一号地 = new Object();
    static Object 二号地 = new Object();
    static Object 三号地 = new Object();
    static Object 四号地 = new Object();


    static Thread 向东跑的车 = new Thread(() -> {
        synchronized (四号地) {
            System.out.println("向东跑的车现在停在四号地上，等待三号地");
            for (int i = 0; i < 1000; i++) {
            }
            synchronized (三号地) {
                System.out.println("向东跑的车离开了");
            }
        }
    }, "向东跑的车");

    static Thread 向西跑的车 = new Thread(() -> {
        synchronized (二号地) {
            System.out.println("向东跑的车现在停在二号地上，等待一号地");
            for (int i = 0; i < 1000; i++) {
            }
            synchronized (一号地) {
                System.out.println("向西跑的车离开了");
            }
        }
    }, "向西跑的车");

    static Thread 向南跑的车 = new Thread(() -> {
        synchronized (一号地) {
            System.out.println("向东跑的车现在停在一号地上，等待四号地");
            for (int i = 0; i < 1000; i++) {
            }
            synchronized (四号地) {
                System.out.println("向南跑的车离开了");
            }
        }
    }, "向南跑的车");

    static Thread 向北跑的车 = new Thread(() -> {
        synchronized (三号地) {
            System.out.println("向东跑的车现在停在三号地上，等待二号地");
            for (int i = 0; i < 1000; i++) {
            }
            synchronized (二号地) {
                System.out.println("向北跑的车离开了");
            }
        }
    }, "向北跑的车");


    public static void main(String[] args) {
        向东跑的车.start();
        向西跑的车.start();
        向南跑的车.start();
        向北跑的车.start();
    }


}
