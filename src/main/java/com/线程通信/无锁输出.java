package com.线程通信;

public class 无锁输出 {
    static volatile boolean 输出数字的线程开启 = true;
    static Thread 输出数字的线程 = new Thread(() -> {
        for (int i = 0; i < 24; i++) {
            while (!输出数字的线程开启) {
            }
            System.out.println("" + i);
            输出数字的线程开启 = false;
        }
    });
    static Thread 输出字母的线程 = new Thread(() -> {
        for (int i = 0; i < 24; i++) {
            while (输出数字的线程开启) {
            }
            int c = 'A' + i;
            System.out.println("     " + (char) c);
            输出数字的线程开启 = true;
        }
    });

    public static void main(String[] args) {
        输出数字的线程.start();
        输出字母的线程.start();
    }
}
