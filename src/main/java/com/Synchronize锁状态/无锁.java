package com.Synchronize锁状态;

import org.openjdk.jol.info.ClassLayout;

public class 无锁 {

    public static void main(String[] args) {
        Object 锁 = new Object();
        new Thread(() -> {
            System.out.println(ClassLayout.parseInstance(锁).toPrintable());
        }).start();

    }

}
