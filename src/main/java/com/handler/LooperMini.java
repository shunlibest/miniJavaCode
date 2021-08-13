package com.handler;


/**
 * 用于为线程运行消息循环的类。
 * 默认情况下，线程没有与之关联的消息循环； 要创建一个，在运行循环的线程中调用prepare ，然后loop让它处理消息，直到循环停止。
 * 大多数与消息循环的交互是通过Handler类进行的。
 * 这是一个Looper线程实现的典型例子，利用prepare和loop的分离，创建了一个初始Handler与Looper进行通信。
 *     class LooperThread extends Thread {
 *         public Handler mHandler;
 *
 *         public void run() {
 *             Looper.prepare();
 *
 *             mHandler = new Handler() {
 *                 public void handleMessage(Message msg) {
 *                     // process incoming messages here
 *                 }
 *             };
 *
 *             Looper.loop();
 *         }
 *     }
 */
public class LooperMini {
    // 除非您调用了 prepare()，否则 sThreadLocal.get() 将返回 null。
    private static final ThreadLocal<LooperMini> sThreadLocal = new ThreadLocal<>();

    final MessageQueueMini mQueueMini;
    volatile boolean mRun;
    Thread mThread;
    private static LooperMini mMainLooperMini = null;

    //将当前线程初始化为循环程序。 这使您有机会创建处理程序，然后在实际开始循环之前引用此循环程序。 调用此方法后一定要调用loop() ，并通过调用quit()结束它。
    public static void prepare() {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new LooperMini());
    }

    /** Initialize the current thread as a looper, marking it as an application's main
     *  looper. The main looper for your application is created by the Android environment,
     *  so you should never need to call this function yourself.
     * {@link #prepare()}
     */

    public static void prepareMainLooper() {
        prepare();
        setMainLooper(myLooper());
//        if (Process.supportsProcesses()) {
//            myLooper().mQueueMini.mQuitAllowed = false;
//        }
    }

    private synchronized static void setMainLooper(LooperMini looperMini) {
        mMainLooperMini = looperMini;
    }

    /** Returns the application's main looper, which lives in the main thread of the application.
     */
    public synchronized static final LooperMini getMainLooper() {
        return mMainLooperMini;
    }

    //在该线程中运行消息队列
    public static void loop() {
        LooperMini me = myLooper();
        MessageQueueMini queue = me.mQueueMini;
        while (true) {
            MessageMini msg = queue.next(); // might block
            if (msg.target == null) {
                // No target is a magic identifier for the quit message.
                return;
            }
//                me.mLogging.println(
//                        ">>>>> Dispatching to " + msg.target + " "
//                                + msg.callback + ": " + msg.what
//                );
            msg.target.dispatchMessage(msg);
//                me.mLogging.println(
//                        "<<<<< Finished to    " + msg.target + " "
//                                + msg.callback);
            msg.recycle();
        }
    }

    //返回与当前线程关联的 Looper 对象
    public static LooperMini myLooper() {
        return sThreadLocal.get();
    }



    public static MessageQueueMini myQueue() {
        return myLooper().mQueueMini;
    }

    private LooperMini() {
        mQueueMini = new MessageQueueMini();
        mRun = true;
        mThread = Thread.currentThread();
    }

    public void quit() {
        MessageMini msg = MessageMini.obtain();
        // 注意：通过直接入队到消息队列中，消息会留下一个空目标。这就是我们如何知道这是一个退出消息。
        mQueueMini.enqueueMessage(msg, 0);
    }


    public Thread getThread() {
        return mThread;
    }

    public MessageQueueMini getQueue() {
        return mQueueMini;
    }

//    public void dump(Printer pw, String prefix) {
//        pw.println(prefix + this);
//        pw.println(prefix + "mRun=" + mRun);
//        pw.println(prefix + "mThread=" + mThread);
//        pw.println(prefix + "mQueue=" + ((mQueueMini != null) ? mQueueMini : "(null"));
//        if (mQueueMini != null) {
//            synchronized (mQueueMini) {
//                long now = SystemClock.uptimeMillis();
//                Message msg = mQueueMini.mMessages;
//                int n = 0;
//                while (msg != null) {
//                    pw.println(prefix + "  Message " + n + ": " + msg.toString(now));
//                    n++;
//                    msg = msg.next;
//                }
//                pw.println(prefix + "(Total messages: " + n + ")");
//            }
//        }
//    }

}

