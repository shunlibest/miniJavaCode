package com.线程池;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolTest {

    public static void main(String[] args) {

        //速度快
        ExecutorService executor = Executors.newCachedThreadPool();

        //速度中，可以自定义核心线程数
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

        // 核心员工只有一个线程，最大线程数量也是1
        ThreadPoolExecutor singleThreadExecutor = ExecutorsMini.newSingleThreadExecutor();


        System.out.println("执行前-核心线程数："+singleThreadExecutor.getActiveCount());

        for (int i = 0; i < 10; i++) {
            singleThreadExecutor.execute(new MyTask("name"+i));
        }
        System.out.println("执行后-核心线程数："+singleThreadExecutor.getActiveCount());



    }




}
