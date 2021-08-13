package com.handler;


import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;

/**
 * Handler 允许您发送和处理与线程的MessageQueue关联的Message和 Runnable 对象。
 * 每个Handler实例都与一个线程和该线程的消息队列相关联。
 * 当您创建一个新的 Handler 时，它会绑定到创建它的线程的线程/消息队列——从那时起，它会将消息和可运行对象传递到该消息队列，并在它们从消息中出来时执行它们队列。
 * <p>
 * Handler 有两个主要用途：
 * (1) 安排消息和可运行对象在将来的某个时间点执行；
 * (2) 将要在与您自己的线程不同的线程上执行的操作排入队列。
 * 调度消息是通过post 、 postAtTime(Runnable, long) 、 postDelayed 、 sendEmptyMessage 、 sendMessage 、 sendMessageAtTime和sendMessageDelayed方法完成的。
 * post版本允许您将 Runnable 对象排入队列，以便在接收到消息队列时调用它们；
 * sendMessage版本允许您将handleMessage数据的Message对象排入handleMessage ，这些数据将由 Handler 的handleMessage方法处理（要求您实现 Handler 的子类）。
 * 当发布或发送到处理程序时，您可以允许在消息队列准备好处理该项目时立即处理该项目，或者指定在处理它之前的延迟或处理它的绝对时间。
 * 后两者允许您实现超时、滴答和其他基于时间的行为。
 * 当为您的应用程序创建进程时，其主线程专用于运行消息队列，该队列负责管理顶级应用程序对象（活动、广播接收器等）及其创建的任何窗口。
 * <p>
 * 您可以创建自己的线程，并通过 Handler 与主应用程序线程进行通信。 这是通过调用与以前相同的post或sendMessage方法来完成的，但来自您的新线程。
 * 给定的 Runnable 或 Message 将被安排在 Handler 的消息队列中，并在适当的时候进行处理。
 */
public class HandlerMini {
    private static final String TAG = "Handler";

    // 用于检测内存泄漏；将此标志设置为 true 以检测扩展此 Handler 类且非静态的匿名、本地或成员类。
    private static final boolean FIND_POTENTIAL_LEAKS = false;

    //可以在实例化 Handler 时使用的回调接口，以避免必须实现您自己的 Handler 子类。
    public interface Callback {
        public boolean handleMessage(MessageMini msg);
    }

    /**
     * Subclasses must implement this to receive messages.
     */
    public void handleMessage(MessageMini msg) {
    }

    //处理分发消息
    public void dispatchMessage(@NotNull MessageMini msg) {
        if (msg.callback != null) {
            handleCallback(msg);
        } else {
            if (mCallback != null) {
                if (mCallback.handleMessage(msg)) {
                    return;
                }
            }
            handleMessage(msg);
        }
    }

    //默认构造函数将此处理程序与当前线程的队列相关联
    public HandlerMini() {
        this((Callback) null);
    }

    //默认构造函数将此处理程序与当前线程的队列相关联
    public HandlerMini(Callback callback) {
        //当检测到内存泄漏的时候
        if (FIND_POTENTIAL_LEAKS) {
            final Class<? extends HandlerMini> klass = getClass();
            if ((klass.isAnonymousClass() || klass.isMemberClass() || klass.isLocalClass()) &&
                    (klass.getModifiers() & Modifier.STATIC) == 0) {
                System.out.println("The following Handler class should be static or leaks might occur: " +
                        klass.getCanonicalName());
            }
        }

        mLooperMini = LooperMini.myLooper();
        if (mLooperMini == null) {
            throw new RuntimeException(
                    "Can't create handler inside thread that has not called Looper.prepare()");
        }
        mQueue = mLooperMini.mQueueMini;
        mCallback = callback;
    }

    /**
     * Use the provided queue instead of the default one.
     */
    public HandlerMini(LooperMini looperMini) {
        mLooperMini = looperMini;
        mQueue = looperMini.mQueueMini;
        mCallback = null;
    }

    /**
     * Use the provided queue instead of the default one and take a callback
     * interface in which to handle messages.
     */
    public HandlerMini(LooperMini looperMini, Callback callback) {
        mLooperMini = looperMini;
        mQueue = looperMini.mQueueMini;
        mCallback = callback;
    }


    public final MessageMini obtainMessage() {
        return MessageMini.obtain(this);
    }

    public final MessageMini obtainMessage(int what) {
        return MessageMini.obtain(this, what);
    }

    public final MessageMini obtainMessage(int what, Object obj) {
        return MessageMini.obtain(this, what, obj);
    }

    public final MessageMini obtainMessage(int what, int arg1, int arg2) {
        return MessageMini.obtain(this, what, arg1, arg2);
    }

    public final MessageMini obtainMessage(int what, int arg1, int arg2, Object obj) {
        return MessageMini.obtain(this, what, arg1, arg2, obj);
    }


    //////////////////发送消息///////////////
    public final boolean post(Runnable r) {
        return sendMessageDelayed(getPostMessage(r), 0);
    }

    public final boolean postAtTime(Runnable r, long uptimeMillis) {
        return sendMessageAtTime(getPostMessage(r), uptimeMillis);
    }

    public final boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
        return sendMessageAtTime(getPostMessage(r, token), uptimeMillis);
    }

    public final boolean postDelayed(Runnable r, long delayMillis) {
        return sendMessageDelayed(getPostMessage(r), delayMillis);
    }

    /**
     * Posts a message to an object that implements Runnable.
     * Causes the Runnable r to executed on the next iteration through the
     * message queue. The runnable will be run on the thread to which this
     * handler is attached.
     * <b>This method is only for use in very special circumstances -- it
     * can easily starve the message queue, cause ordering problems, or have
     * other unexpected side-effects.</b>
     *
     * @param r The Runnable that will be executed.
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     */
    public final boolean postAtFrontOfQueue(Runnable r) {
        return sendMessageAtFrontOfQueue(getPostMessage(r));
    }

    public final boolean sendMessage(MessageMini msg) {
        return sendMessageDelayed(msg, 0);
    }

    public final boolean sendEmptyMessage(int what) {
        return sendEmptyMessageDelayed(what, 0);
    }

    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        MessageMini msg = MessageMini.obtain();
        msg.what = what;
        return sendMessageDelayed(msg, delayMillis);
    }

    public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        MessageMini msg = MessageMini.obtain();
        msg.what = what;
        return sendMessageAtTime(msg, uptimeMillis);
    }

    public final boolean sendMessageDelayed(MessageMini msg, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return sendMessageAtTime(msg, System.currentTimeMillis() + delayMillis);
    }

    public boolean sendMessageAtTime(MessageMini msg, long uptimeMillis) {
        boolean sent = false;
        MessageQueueMini queue = mQueue;
        if (queue != null) {
            msg.target = this;
            sent = queue.enqueueMessage(msg, uptimeMillis);
        } else {
            RuntimeException e = new RuntimeException(
                    this + " sendMessageAtTime() called with no mQueue");
            System.out.println(e.getMessage());
        }
        return sent;
    }

    /**
     * Enqueue a message at the front of the message queue, to be processed on
     * the next iteration of the message loop.  You will receive it in
     * {@link #handleMessage}, in the thread attached to this handler.
     * <b>This method is only for use in very special circumstances -- it
     * can easily starve the message queue, cause ordering problems, or have
     * other unexpected side-effects.</b>
     *
     * @return Returns true if the message was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     */
    public final boolean sendMessageAtFrontOfQueue(MessageMini msg) {
        boolean sent = false;
        MessageQueueMini queue = mQueue;
        if (queue != null) {
            msg.target = this;
            sent = queue.enqueueMessage(msg, 0);
        } else {
            System.out.println("sendMessageAtTime() called with no mQueue");
        }
        return sent;
    }


    //////////////////////////从消息队列中移除消息//////////////////////////////////
    public final void removeMessages(int what) {
        mQueue.removeCallbacksAndMessages(this, what);
    }

    public final void removeCallbacksAndMessages(Object token) {
        mQueue.removeCallbacksAndMessages(this, token);
    }

    /**
     * Check if there are any pending posts of messages with code 'what' in
     * the message queue.
     */
    public final boolean hasMessages(int what) {
        return mQueue.hasMessages(this, what, null, false);
    }

    public final boolean hasMessages(int what, Object object) {
        return mQueue.hasMessages(this, what, object, false);
    }

    // if we can get rid of this method, the handler need not remember its loop
    // we could instead export a getMessageQueue() method...
    public final LooperMini getLooper() {
        return mLooperMini;
    }




    private final MessageMini getPostMessage(Runnable r) {
        MessageMini m = MessageMini.obtain();
        m.callback = r;
        return m;
    }

    private final MessageMini getPostMessage(Runnable r, Object token) {
        MessageMini m = MessageMini.obtain();
        m.obj = token;
        m.callback = r;
        return m;
    }

    private final void handleCallback(MessageMini messageMini) {
        messageMini.callback.run();
    }

    final MessageQueueMini mQueue;
    final LooperMini mLooperMini;
    final Callback mCallback;
}
