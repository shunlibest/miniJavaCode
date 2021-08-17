package com.Synchronize锁状态;

import org.openjdk.jol.info.ClassLayout;

public class 偏向锁 {

    public static void main(String[] args) {
        Object 锁 = new Object();
        System.out.println("申请前"+ClassLayout.parseInstance(锁).toPrintable());

        new Thread(() -> {
            synchronized (锁){
                System.out.println("申请后"+ClassLayout.parseInstance(锁).toPrintable());
            }
        }).start();
    }

}
