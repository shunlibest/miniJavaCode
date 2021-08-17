package com.Synchronize锁状态;

import org.openjdk.jol.info.ClassLayout;

public class 重量级锁 {

    public static void main(String[] args) throws InterruptedException {
        Object 锁 = new Object();
        new Thread(() -> {
            synchronized (锁) {
                try {
                    //睡2S是轻量级锁
                    Thread.sleep(5_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            System.out.println("申请前" + ClassLayout.parseInstance(锁).toPrintable());
            synchronized (锁) {
                System.out.println("申请后"+ClassLayout.parseInstance(锁).toPrintable());
            }

        }).start();

//        System.out.println(ClassLayout.parseInstance(锁).toPrintable());
    }

}
