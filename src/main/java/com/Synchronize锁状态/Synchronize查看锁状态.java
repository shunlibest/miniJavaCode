package com.Synchronize锁状态;

import com.utils.ConsoleColors;
import org.openjdk.jol.info.ClassLayout;

public class Synchronize查看锁状态 {

    static Object 锁A = new Object();
    static Object 锁B = new Object();

    static Thread 小张 = new Thread(() -> {
        synchronized (锁A) {
            System.out.println("小张拿到A锁");

            for (int i = 0; i < 1000000000; i++) {
                for (int j = 0; j < 1000000000; j++) {
                    for (int x = 0; x < 1000000000; x++) {

                    }
                }
            }
        }

    }, "小张");

    static Thread 小李 = new Thread(() -> {
        synchronized (锁A) {
            System.out.println("小张拿到A锁");
            for (int i = 0; i < 1000000000; i++) {
                for (int j = 0; j < 1000000000; j++) {
                    for (int x = 0; x < 1000000000; x++) {

                    }
                }
            }
        }
        System.out.println("小张释放A锁");

    }, "小张");
//
//
//    public static void main(String[] args) throws InterruptedException {
//        System.out.println("小张释放A锁");
//        System.out.println("小张释放A锁");
//        System.out.println("小张释放A锁");
//
//        System.out.println(ConsoleColors.RED + "RED COLORED" +ConsoleColors.RESET + " NORMAL");
//
//
//
//
//        小张.start();
//
////        小李.start();
//        String s = ClassLayout.parseInstance(锁A).toPrintable();
//        System.out.println(s);
//
//
//        String[] split = s.split("0x");
//        String objectHeaderMark = split[1].split(" ")[0];
//        System.out.println(objectHeaderMark);
//
//        long mark = Long.parseLong(objectHeaderMark,16);
//        System.out.println(Long.toHexString(mark));
//
//        long bits = mark & 0b11;
//        System.out.println("bits:"+Long.toBinaryString(bits));
//        System.out.println("mark:"+Long.toBinaryString(mark));
//
//
//        System.out.println();
//        System.out.println(锁A.toString());
//
//
//        System.out.println(ClassLayout.parseInstance(锁A).toPrintable());
//    }

    public static void main(String[] args) throws InterruptedException {

        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Object a = new Object();
        final Thread thread1 = new Thread(() -> {
            synchronized (a) {

                System.out.println("thread1 locking");
                System.out.println(ClassLayout.parseInstance(a).toPrintable());
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2申请");
            System.out.println(ClassLayout.parseInstance(a).toPrintable());
            synchronized (a){
                System.out.println("thread2 locking");
                System.out.println(ClassLayout.parseInstance(a).fields());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        thread2.start();


        for (int i = 0; i < 1000000; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("偏向"+ClassLayout.parseInstance(a).toPrintable());

        }
        System.out.println(thread1.getId());
    }


}
