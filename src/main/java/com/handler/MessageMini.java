package com.handler;


import java.io.Serializable;

/**
 * 定义包含描述和任意数据对象的消息，可以发送到Handler中。
 * 该对象包含两个额外的 int 字段和一个额外的对象字段，允许您在许多情况下不进行分配。
 * 虽然 Message 的构造函数是公共的，但获得其中之一的最佳方法是调用Message.obtain()或Handler.obtainMessage()方法之一，这将从回收对象池中提取它们。
 */
public final class MessageMini implements Serializable {

    //用户定义的消息代码，以便收件人可以识别此消息的内容。相当于初步见面，说一下自己是什么消息
    public int what;

    //如果您只需要存储几个整数值，直接用arg1和arg2
    public int arg1, arg2;

    //发送给接收者的任意对象
    public Object obj;

    //对于其他数据传输，请使用setData 。

    //可选的 Messenger，可以在其中发送对此消息的回复。 具体如何使用的语义取决于发送方和接收方。
//    public Messenger replyTo;

    /*package*/ long when;

//    /*package*/ Bundle data;

    /*package*/ HandlerMini target;

    /*package*/ Runnable callback;

    // sometimes we store linked lists of these things
    /*package*/ MessageMini next;


    //这里有4个静态变量，只有一个目的，就是不用新买一封信，而且把之前用过的，垃圾桶里的无用信纸，重写涂改，写上新内容。
    //源码锁写的方式有缺陷，锁不是final类型的，可以被修改
    //private static Object mPoolSync = new Object();
    private static final Object mPoolSync = new Object();
    //存储垃圾message的链表
    private static MessageMini mPool;
    //链表长度
    private static int mPoolSize = 0;
    private static final int MAX_POOL_SIZE = 10;

    //等价于Message.obtain()
    public MessageMini() {
    }

    //从全局池中返回一个新的 Message 实例。
    public static MessageMini obtain() {
        synchronized (mPoolSync) {
            if (mPool != null) {
                MessageMini m = mPool;
                mPool = m.next;
                m.next = null;
                return m;
            }
        }
        return new MessageMini();
    }

    /**
     * Same as {@link #obtain()}, but copies the values of an existing
     * message (including its target) into the new one.
     *
     * @param orig Original message to copy.
     * @return A Message object from the global pool.
     */
    public static MessageMini obtain(MessageMini orig) {
        MessageMini m = obtain();
        m.what = orig.what;
        m.arg1 = orig.arg1;
        m.arg2 = orig.arg2;
        m.obj = orig.obj;
//        m.replyTo = orig.replyTo;
//        if (orig.data != null) {
//            m.data = new Bundle(orig.data);
//        }
        m.target = orig.target;
        m.callback = orig.callback;

        return m;
    }

    //在返回的 Message 上设置目标成员的值。
    public static MessageMini obtain(HandlerMini h) {
        MessageMini m = obtain();
        m.target = h;
        return m;
    }

    public static MessageMini obtain(HandlerMini h, Runnable callback) {
        MessageMini m = obtain();
        m.target = h;
        m.callback = callback;
        return m;
    }

    public static MessageMini obtain(HandlerMini h, int what) {
        MessageMini m = obtain();
        m.target = h;
        m.what = what;
        return m;
    }

    public static MessageMini obtain(HandlerMini h, int what, Object obj) {
        MessageMini m = obtain();
        m.target = h;
        m.what = what;
        m.obj = obj;

        return m;
    }

    public static MessageMini obtain(HandlerMini h, int what, int arg1, int arg2) {
        MessageMini m = obtain();
        m.target = h;
        m.what = what;
        m.arg1 = arg1;
        m.arg2 = arg2;
        return m;
    }

    public static MessageMini obtain(HandlerMini h, int what, int arg1, int arg2, Object obj) {
        MessageMini m = obtain();
        m.target = h;
        m.what = what;
        m.arg1 = arg1;
        m.arg2 = arg2;
        m.obj = obj;
        return m;
    }

    //把消费的消息放到全局池
    public void recycle() {
        synchronized (mPoolSync) {
            if (mPoolSize < MAX_POOL_SIZE) {
                clearForRecycle();
                next = mPool;
                mPool = this;
            }
        }
    }


    public long getWhen() {
        return when;
    }


    //将此消息发送到由getTarget指定的处理程序
    public void sendToTarget() {
        target.sendMessage(this);
    }

    //抹去回收池里的message里的内容
    void clearForRecycle() {
        what = 0;
        arg1 = 0;
        arg2 = 0;
        obj = null;
//        replyTo = null;
        when = 0;
        target = null;
        callback = null;
//        data = null;
    }


    /////////////后面省略序列化部分/////////////////////////
}