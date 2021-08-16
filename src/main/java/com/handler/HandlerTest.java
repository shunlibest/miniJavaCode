package com.handler;

import java.util.Scanner;

public class HandlerTest {

    public static void main(String[] args) {
        HandlerTest handlerTest = new HandlerTest();

        handlerTest.newThread();

        handlerTest.消息发送者();
    }

    HandlerMini childHandler ;


    void  newThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String msg;
                LooperMini.prepare();

//                Looper.getMainLooper();

                childHandler = new HandlerMini() {
                    @Override
                    public void handleMessage(MessageMini msg) {
                        super.handleMessage(msg);
                        System.out.println(Thread.currentThread().getName());

                        System.out.println("这个消息是从-->>" + msg.obj.toString() + "过来的，在" + "btn的子线程当中" + "中执行的");
                    }

                };

                LooperMini.loop();//开始轮循
            }

        });
        thread.start();

    }

    public void 消息发送者() {


            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        System.out.println("发送消息");
                        Scanner sc = new Scanner(System.in);
                        final String  str = sc.nextLine();
                        System.out.println(str);
                        MessageMini messageMini = childHandler.obtainMessage(110, str);
                        childHandler.sendMessage(messageMini);
                    }



                }
            }).start();

    }
}
