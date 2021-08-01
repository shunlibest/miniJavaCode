package com.collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;


/**
 * 此类提供Collection接口的骨架实现，以最大限度地减少实现此接口所需的工作。
 * 为了实现一个不可修改的集合，程序员只需要扩展这个类并提供迭代器和大小方法的实现，迭代器方法返回的迭代器必须实现hasNext和next。
 * 要实现可修改的集合，程序员必须另外覆盖此类的add方法，并且迭代器方法返回的迭代器必须另外实现其remove方法。
 * 根据Collection接口规范中的建议，程序员通常应提供 void（无参数）和Collection构造函数。
 * 此类中每个非抽象方法的文档详细描述了其实现。 如果正在实现的集合允许更有效的实现，则可以覆盖这些方法中的每一个。
 *
 * @author Josh Bloch
 * @author Neal Gafter
 */

public abstract class AbstractCollectionMini<E> implements CollectionMini<E> {
    //唯一的构造函数，对于子类构造函数的调用，通常是隐藏的
    protected AbstractCollectionMini() {
    }

    // 查询操作

    /**
     * 返回此集合中包含的元素的迭代器。
     **/
    public abstract Iterator<E> iterator();

    @Override
    public abstract int size();

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    //包含操作
    @Override
    public boolean contains(@Nullable Object o) {
        Iterator<E> it = iterator();
        if (o == null) {
            while (it.hasNext())
                if (it.next() == null)
                    return true;
        } else {
            while (it.hasNext())
                if (o.equals(it.next()))
                    return true;
        }
        return false;
    }

    /**
     * 此实现返回一个数组，其中包含此集合的迭代器返回的所有元素，以相同的顺序存储在数组的连续元素中，
     * 从索引0开始。 返回的数组的长度等于迭代器返回的元素数，
     * 如果集合的大小在迭代期间发生变化，则可能会发生这种情况。 size方法仅作为优化提示被调用；
     * 即使迭代器返回不同数量的元素，也会返回正确的结果。
     * <p>
     * 此方法等效于：
     * List<E> list = new ArrayList<E>(size());
     * for (E e : this)
     * list.add(e);
     * return list.toArray();
     */
    public Object[] toArray() {
        //先计算一下数组的大小，集合元素可能会发生变化
        Object[] r = new Object[size()];
        Iterator<E> it = iterator();
        for (int i = 0; i < r.length; i++) {
            //也就是集合中真正的元素，比实际大小小，所以就另建一个数组，拷贝过去
            if (!it.hasNext())
                return Arrays.copyOf(r, i);
            r[i] = it.next();
        }
        //当迭代器返回的元素多于预期时
        return it.hasNext() ? finishToArray(r, it) : r;
    }


    // public <T> T[] toArray(T[] a) {  }

    /**
     * 要分配的数组的最大大小。
     * 一些 VM 在数组中保留一些头字。 尝试分配更大的数组可能会导致 OutOfMemoryError：请求的数组大小超出 VM 限制
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 当迭代器返回的元素多于预期时，重新分配在 toArray 中使用的数组，并完成从迭代器填充它。
     * 参数：
     * r – 数组，充满了先前存储的元素
     * it – 此集合的正在进行的迭代器
     * 返回：包含给定数组中元素的数组，加上迭代器返回的任何其他元素
     */
    private static <T> T[] finishToArray(T[] r, Iterator<?> it) {
        //数组现在初始时的元素长度
        int i = r.length;
        while (it.hasNext()) {
            //数组大小被修改过后的长度
            int cap = r.length;
            //开始扩容数组
            if (i == cap) {
                int newCap = cap + (cap >> 1) + 1;
                // 溢出意识代码
                if (newCap - MAX_ARRAY_SIZE > 0)
                    newCap = hugeCapacity(cap + 1);
                r = Arrays.copyOf(r, newCap);
            }
            r[i++] = (T) it.next();
        }
        // 如果申请的数组长度超过集合的长度，那就只拷贝其中有用的代码
        return (i == r.length) ? r : Arrays.copyOf(r, i);
    }

    //获取最大能申请的集合数量
    static int hugeCapacity(int minCapacity) {
        return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }

    /**
     * 集合中移除指定元素的单个实例（如果存在）。 并且包含方法差不多，可能会一次移除多个元素
     * 此实现遍历集合以查找指定元素。 如果找到该元素，则使用迭代器的 remove 方法从集合中删除该元素。
     */
    @Override
    public boolean remove(Object o) {
        Iterator<E> it = iterator();
        if (o == null) {
            while (it.hasNext()) {
                if (it.next() == null) {
                    it.remove();
                    return true;
                }
            }
        } else {
            while (it.hasNext()) {
                if (o.equals(it.next())) {
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }

    //依次检查迭代器返回的每个元素以查看它是否包含在此集合中
    @Override
    public boolean containsAll(@NotNull CollectionMini<?> c) {
        Objects.requireNonNull(c);
        for (Iterator<?> iterator = c.iterator(); iterator.hasNext(); ) {
            if (!contains(iterator)) {
                return false;
            }
        }
        return true;
    }

    //将迭代器返回的每个对象依次添加到此集合中
    @Override
    public boolean addAll(@Nullable CollectionMini<? extends E> c) {
        boolean modified = false;
//        for (Iterator<? extends E> e = c.iterator(); e.hasNext(); ) {
//            if (add(e))
//                modified = true;
//        }
        return modified;
    }

    /**
     * 此实现遍历此集合，依次检查迭代器返回的每个元素以查看它是否包含在指定的集合中。
     * 如果它被包含在内，它将使用迭代器的remove方法从这个集合中删除。
     */
    @Override
    public boolean removeAll(CollectionMini<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<?> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * 依次检查迭代器返回的每个元素以查看它是否包含在指定的集合中。
     * 如果它不是这样包含的，它将使用迭代器的remove方法从这个集合中删除。
     */
    @Override
    public boolean retainAll(CollectionMini<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    //使用Iterator.remove操作删除每个元素。
    //大多数实现可能会选择覆盖此方法以提高效率。
    public void clear() {
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }


    // 经典的toString方法
    public String toString() {
        Iterator<E> it = iterator();
        //空集合的情况
        if (!it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (; ; ) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            //最后一个元素
            if (!it.hasNext()) {
                return sb.append(']').toString();
            } else {
                sb.append(',').append(' ');

            }
        }
    }

}
