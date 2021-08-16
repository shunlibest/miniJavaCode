package com.线程通信;

public class 无约束输出 {
    static Thread 输出数字的线程 = new Thread(() -> {
        for (int i = 0; i < 24; i++) {
            System.out.println("" + i);
        }
    });

    static Thread 输出字母的线程 = new Thread(() -> {
        for (int i = 0; i < 24; i++) {
            int c = 'A' + i;
            System.out.println("     " + (char) c);
        }
    });

    public static void main(String[] args) {
        输出数字的线程.start();
        输出字母的线程.start();
    }
}
