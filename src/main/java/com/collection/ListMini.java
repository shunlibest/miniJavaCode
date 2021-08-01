package com.collection;


import java.util.Arrays;
import java.util.Comparator;

/**
 * 有序集合（也称为序列）。
 * 此界面的用户可以精确控制每个元素在列表中的插入位置。 用户可以通过它们的整数索引（在列表中的位置）访问元素，并在列表中搜索元素。
 * 与集合不同，列表通常允许重复元素。也就是e1.equals(e2) 。
 * List接口在迭代器、 add 、 remove 、 equals和hashCode方法的契约上放置了除Collection接口中指定的那些之外的其他规定。
 * List接口提供了四种对列表元素位置访问的方法。
 * 请注意，对于某些实现（例如LinkedList类），这些操作的执行时间可能与索引值成正比。 因此，如果调用者不知道实现，则迭代列表中的元素通常比通过它进行索引更可取。
 * List接口提供了一个特殊的迭代器，称为ListIterator ，除了Iterator接口提供的正常操作之外，它允许元素插入和替换以及双向访问。
 * List接口提供了两种方法来搜索指定的对象。 从性能的角度来看，应谨慎使用这些方法。 在许多实现中，它们将执行代价高昂的线性搜索。
 * List接口提供了两种方法来有效地在列表中的任意点插入和删除多个元素。
 * 注意：虽然允许列表将自身包含为元素，但建议格外小心： equals和hashCode方法不再在此类列表上明确定义。
 * 一些列表实现对它们可能包含的元素有限制。
 * 例如，有些实现禁止空元素，有些实现对其元素的类型有限制。
 */

public interface ListMini<E> extends CollectionMini<E> {


    //将指定的元素附加到此列表的末尾
    boolean add(E e);

    //从此列表中删除第一次出现的指定元素，也就是索引值最小的元素
    boolean remove(Object o);

    /**
     * 根据由指定Comparator引起的顺序对此列表进行排序。
     * 此列表中的所有元素必须使用指定的比较器相互比较（即 c.compare(e1, e2)）。
     * 如果指定的比较器为null则此列表中的所有元素都必须实现Comparable接口，并且应使用元素的自然顺序。
     * 此列表必须是可修改的，但不需要可调整大小。
     * <p>
     * 参数：c – 用于比较列表元素的Comparator 。 null值表示应该使用元素的自然顺序
     * <p>
     * 实施要求：默认实现获取一个包含此列表中所有元素的数组，对数组进行排序，并迭代此列表，从数组中的相应位置重置每个元素。（这避免了因尝试对链接列表进行排序而导致的 n 2 log(n) 性能。）
     * <p>
     * 实施注意事项：这种实现是一种稳定的、自适应的、迭代的归并排序，当输入数组部分排序时，它需要的比较次数远少于 n lg(n) 次，
     * 同时在输入数组随机排序时提供传统归并排序的性能。
     * 如果输入数组几乎已排序，则实现需要大约 n 次比较。 临时存储要求从几乎排序的输入数组的小常量到随机排序的输入数组的 n/2 个对象引用不等。
     * 该实现在其输入数组中平等地利用升序和降序，并且可以在同一输入数组的不同部分利用升序和降序。 它非常适合合并两个或多个已排序的数组：只需连接数组并对结果数组进行排序。
     * <p>
     * TimSort排序算法
     */
    default void sort(Comparator<? super E> c) {
        Object[] a = this.toArray();
        Arrays.sort(a, (Comparator) c);
//        ListIterator<E> i = this.listIterator();
//        for (Object e : a) {
//            i.next();
//            i.set((E) e);
//        }
    }


    // 位置访问操作
    //返回此列表中指定位置的元素。
    E get(int index);

    //用指定的元素替换此列表中指定位置的元素。
    E set(int index, E element);

    //在此列表中的指定位置插入指定元素，后续元素将向后移动一位。
    void add(int index, E element);

    //移除此列表中指定位置的元素，后面的元素，将会向前移动一位
    E remove(int index);


    // 搜索操作

    //返回此列表中指定元素第一次出现的索引，如果此列表不包含该元素，则返回 -1。
    int indexOf(Object o);

    //返回此列表中指定元素最后一次出现的索引
    int lastIndexOf(Object o);

    // View

    /**
     * 返回此列表中指定的fromIndex和toIndex之间的部分的视图。（如果fromIndex和toIndex相等，则返回的列表为空）
     * 例如，以下习语从列表中删除一系列元素：
     * list.subList(from, to).clear();
     * 可以为indexOf和lastIndexOf构造类似的习惯用法，并且Collections类中的所有算法都可以应用于子列表。
     * 如果支持列表（即，此列表）以除通过返回列表以外的任何方式在结构上进行了修改，则此方法返回的列表的语义将变为未定义。
     * （结构修改是那些改变这个列表的大小，或者以其他方式扰乱它，以致正在进行的迭代可能会产生不正确的结果。）
     */
    ListMini<E> subList(int fromIndex, int toIndex);
}
