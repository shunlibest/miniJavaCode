package com.handler;


import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * 包含要由 {@link LooperMini} 调度的消息列表的低级类。消息不会直接添加到 MessageQueue，而是通过与 Looper 关联的 {@link HandlerMini} 对象。
 * 检索当前线程的 MessageQueue。
 */
public class MessageQueueMini {
    //消息队列，本质上是一个单向链表，这里引用的是一个头结点
    MessageMini mMessages;
    private final ArrayList<IdleHandler> mIdleHandlers = new ArrayList<IdleHandler>();
    private IdleHandler[] mPendingIdleHandlers;
    private boolean mQuiting;
    boolean mQuitAllowed = true;

    // 指示 next() 是否在 pollOnce() 中以非零超时阻塞等待。
    private boolean mBlocked;

    @SuppressWarnings("unused")
    private int mPtr; // used by native code

//    private native void nativeInit();
//
//    private native void nativeDestroy();
//
//    private native void nativePollOnce(int ptr, int timeoutMillis);
//
//    private native void nativeWake(int ptr);


    MessageQueueMini() {
//        nativeInit();
    }


    @Override
    protected void finalize() throws Throwable {
        try {
//            nativeDestroy();
        } finally {
            super.finalize();
        }
    }

    /**
     * Callback interface for discovering when a thread is going to block
     * waiting for more messages.
     */
    public static interface IdleHandler {
        /**
         * Called when the message queue has run out of messages and will now
         * wait for more.  Return true to keep your idle handler active, false
         * to have it removed.  This may be called if there are still messages
         * pending in the queue, but they are all scheduled to be dispatched
         * after the current time.
         */
        boolean queueIdle();
    }

    /**
     * Add a new {@link IdleHandler} to this message queue.  This may be
     * removed automatically for you by returning false from
     * {@link IdleHandler#queueIdle IdleHandler.queueIdle()} when it is
     * invoked, or explicitly removing it with {@link #removeIdleHandler}.
     *
     * <p>This method is safe to call from any thread.
     *
     * @param handler The IdleHandler to be added.
     */
    public final void addIdleHandler(IdleHandler handler) {
        if (handler == null) {
            throw new NullPointerException("Can't add a null IdleHandler");
        }
        synchronized (this) {
            mIdleHandlers.add(handler);
        }
    }

    /**
     * Remove an {@link IdleHandler} from the queue that was previously added
     * with {@link #addIdleHandler}.  If the given object is not currently
     * in the idle list, nothing is done.
     *
     * @param handler The IdleHandler to be removed.
     */
    public final void removeIdleHandler(IdleHandler handler) {
        synchronized (this) {
            mIdleHandlers.remove(handler);
        }
    }

    final Object 锁 = new Object();


    //第一步：发送消息，会把消息发送到队列中
    //这个方法主要是用来处理发送消息的，当Handler通过自己enqueueMessage()将消息发送到这该函数中。
    //msg:要发送的消息内容
    //when:延迟多久发送
    final boolean enqueueMessage(MessageMini msg, long when) {
        //同一条消息，只能被用一次，也就说明，同一条消息，只能发送一次
        if (msg.when != 0) {
            throw new RuntimeException(msg + " This message is already in use.");
        }
        if (msg.target == null && !mQuitAllowed) {
            throw new RuntimeException("Main thread not allowed to quit");
        }
        final boolean needWake;
        synchronized (this) {
            if (mQuiting) {
                System.out.println("sending message to a Handler on a dead thread");
                return false;
            } else if (msg.target == null) {
                mQuiting = true;
            }
            msg.when = when;

            MessageMini p = mMessages;
            //把新的消息放到头结点的几种情况：
            //①该链表本来就是空链表
            //②新消息不需要等待，是立即发送型的
            //③新消息的发送时间 比 现在头结点的发送事件要早
            if (p == null || when == 0 || when < p.when) {
                msg.next = p;
                mMessages = msg;
                needWake = mBlocked; // new head, might need to wake up
            } else {
                MessageMini prev = null;
                //相当于一个时间轴把，按照时间顺序，把新消息插入到对应位置
                while (p != null && p.when <= when) {
                    prev = p;
                    p = p.next;
                }
                //插入
                msg.next = prev.next;
                prev.next = msg;

                needWake = false; // still waiting on head, no need to wake up
            }

            this.notify();
        }
        // 如果looper阻塞/休眠中，则唤醒looper循环机制处理消息
//        if (needWake) {
////            nativeWake(mPtr);
//
//        }


        return true;
    }


    //获取最近收到的一条短信
    final synchronized  MessageMini next() {
        int pendingIdleHandlerCount = -1; // -1 仅在第一次迭代期间

        //0，立即返回，没有阻塞；
        //负数，一直阻塞，直到事件发生；
        //正数，表示最多等待多久时间；
        //而我们传入的nextPollTimeoutMillis = -1，此时会一直阻塞。
        int nextPollTimeoutMillis = 0;

        for (; ; ) {
            if (nextPollTimeoutMillis != 0) {
                // 将当前线程中挂起的任何绑定器命令刷新到内核驱动程序。
                // 在执行可能会阻塞很长时间的操作之前调用此方法非常有用，可以确保释放了所有挂起的对象引用，以防止进程持有对象的时间超过需要的时间。
//                BinderMini.flushPendingCommands();
            }
            //相当于在这等待Sleep(nextPollTimeoutMillis)
//            nativePollOnce(mPtr, nextPollTimeoutMillis);
//            锁

//            synchronized (锁){
                try {
                    wait(nextPollTimeoutMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//            }



            synchronized (this) {
                // 尝试检查下一条消息。找到就返回。
                final long now = System.currentTimeMillis();
                final MessageMini msg = mMessages;
                if (msg != null) {
                    final long when = msg.when;
                    //找到消息，并且刚刚超过消息的延迟事件
                    if (now >= when) {
                        mBlocked = false;
                        mMessages = msg.next;
                        msg.next = null;
                        return msg;
                    } else {
                        //比如要延迟发送短信，现在是3点，msg.when=5点，nextPollTimeoutMillis代表2个小时后再来检查一下
                        nextPollTimeoutMillis = (int) Math.min(when - now, Integer.MAX_VALUE);
                    }
                } else {
                    //要是真的没有收到消息，就干等着
                    nextPollTimeoutMillis = -1;
                }

                // 如果是第一次，则获取要运行的空闲程序的数量。
                if (pendingIdleHandlerCount < 0) {
                    pendingIdleHandlerCount = mIdleHandlers.size();
                }
                if (pendingIdleHandlerCount == 0) {
                    // 没有要运行的空闲处理程序。循环再等一会儿。
                    mBlocked = true;
                    continue;
                }

                if (mPendingIdleHandlers == null) {
                    mPendingIdleHandlers = new IdleHandler[Math.max(pendingIdleHandlerCount, 4)];
                }
                mPendingIdleHandlers = mIdleHandlers.toArray(mPendingIdleHandlers);
            }

            // Run the idle handlers.
            // We only ever reach this code block during the first iteration.
            for (int i = 0; i < pendingIdleHandlerCount; i++) {
                final IdleHandler idler = mPendingIdleHandlers[i];
                mPendingIdleHandlers[i] = null; // release the reference to the handler

                boolean keep = false;
                try {
                    keep = idler.queueIdle();
                } catch (Throwable t) {
                    System.out.println("IdleHandler threw exception");
                }

                if (!keep) {
                    synchronized (this) {
                        mIdleHandlers.remove(idler);
                    }
                }
            }

            // Reset the idle handler count to 0 so we do not run them again.
            pendingIdleHandlerCount = 0;

            // While calling an idle handler, a new message could have been delivered
            // so go back and look again for a pending message without waiting.
            nextPollTimeoutMillis = 0;
        }
    }

    //////////////////////////从消息队列里，移除某一条消息///////////////////////////////
    //吐槽值拉满，这里竟然把【删除】和【是否查找到】的逻辑写在一起。👻鬼才操作👻
    //原名removeMessages
    final boolean hasMessages(HandlerMini h, int what, Object object, boolean doRemove) {
        MessageMini p = mMessages;
        while (p != null) {
            if (p.target == h && p.what == what && (object == null || p.obj == object)) {
                return true;
            }
            p = p.next;
        }
        return false;
    }

    //吐槽一下，这里写的属实有点👎🏻。源码里是复制代码再改个名字(连注释都一样)，新增参数，没有使用重载
    final void removeCallbacksAndMessages(HandlerMini h, @Nullable Object object) {
        removeCallbacksAndMessages(h, object, null);
    }

    //这里一个很普通，很经典的链表删除算法
    final void removeCallbacksAndMessages(HandlerMini h, @Nullable Object object, @Nullable Runnable r) {
        synchronized (this) {
            MessageMini p = mMessages;
            // 遍历链表，找出处理地为，筛选条件为HandlerMini 和 消息内容相等；
            // 如果object为null，就表示清楚HandlerMini下的所有未发送消息
            // 特殊处理：循环遍历删除头结点
            while (p != null && p.target == h && (r == null || p.callback == r) && (object == null || p.obj == object)) {
                MessageMini n = p.next;
                mMessages = n;
                p.recycle();
                p = n;
            }
            while (p != null) {
                MessageMini n = p.next;
                if (n != null) {
                    if (n.target == h && (object == null || n.obj == object)) {
                        MessageMini nn = n.next;
                        n.recycle();
                        p.next = nn;
                        continue;
                    }
                }
                p = n;
            }
        }
    }

    //输出打印当前列表
    private void dumpQueue_l() {
        MessageMini p = mMessages;
        System.out.println(this + "  queue is:");
        while (p != null) {
            System.out.println("            " + p);
            p = p.next;
        }
    }
}
