package com.线程通信;

public class notify输出 {
    static  boolean 输出数字的线程开启 = true;

    static final Object 锁 = new Object();

    static Thread 输出数字的线程 = new Thread(() -> {
        for (int i = 0; i < 24; i++) {
//            while (!输出数字的线程开启) {
//            }
            synchronized (锁){
//                System.out.println("数字获得锁");
                if (!输出数字的线程开启){
                    try {
                        锁.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("" + i);
                输出数字的线程开启 = false;
                锁.notify();
            }

        }
    });
    static Thread 输出字母的线程 = new Thread(() -> {
        for (int i = 0; i < 24; i++) {
            synchronized (锁){
//                System.out.println("---------字母获得锁");

                if (输出数字的线程开启){
                    try {
                        //释放锁，不会进入竞争线程队列
                        锁.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                int c = 'A' + i;
                System.out.println("     " + (char) c);
                输出数字的线程开启 = true;

                //唤醒一条wait中的线程，让那条线程进行竞争队列；
                锁.notify();
            }
        }
    });
//
//    static Thread 输出汉字的线程 = new Thread(() -> {
//        for (int i = 0; i < 24; i++) {
//            synchronized (锁){
//                int c = 'A' + i;
//                System.out.println("     " + (char) c);
//                输出数字的线程开启 = true;
//            }
//        }
//    });

    public static void main(String[] args) {
        输出数字的线程.start();
        输出字母的线程.start();
    }
}
