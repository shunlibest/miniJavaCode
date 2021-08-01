package com.collection;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * 集合层次结构中的根接口。 一个集合代表一组对象，称为它的元素。
 * 一些集合允许重复元素，而另一些则不允许。 有些是有序的，有些是无序的。
 * <p>
 * JDK 不提供此接口的任何直接实现：它提供了更具体的子接口（如Set和List ）的实现。 此接口通常用于传递集合并在需要最大通用性的地方操作它们。
 * Bags或multisets （可能包含重复元素的无序集合）应该直接实现这个接口。
 * <p>
 * 所有通用Collection实现类（通常通过其子接口之一间接实现Collection ）应该提供两个“标准”构造函数：
 * 一个 void（无参数）构造函数，它创建一个空集合，以及一个具有单个参数类型的构造函数Collection ，它创建一个具有与其参数相同的元素的新集合。
 * 实际上，后一个构造函数允许用户复制任何集合，生成所需实现类型的等效集合。
 * 没有办法强制执行此约定（因为接口不能包含构造函数），但 Java 平台库中的所有通用Collection实现都符合。
 * 此接口中包含的“破坏性”方法，即修改它们操作的集合的方法，如果此集合不支持该操作，则指定为抛出UnsupportedOperationException 。
 * 在这种情况下，如果调用对集合没有影响，则这些方法可能（但不是必需）抛出UnsupportedOperationException 。
 * 例如，如果要添加的集合为空，则在不可修改的集合上调用addAll(Collection)方法可能（但不是必需）抛出异常。
 * 一些集合实现对它们可能包含的元素有限制。 例如，有些实现禁止空元素，有些实现对其元素的类型有限制。 尝试添加不合格的元素会引发未经检查的异常，
 * 通常为NullPointerException或ClassCastException 。 尝试查询不合格元素的存在可能会引发异常，或者可能只是返回 false；
 * 一些实现会表现出前一种行为，而另一些会表现出后者。
 * 更一般地，尝试对不合格元素执行操作，其完成不会导致将不合格元素插入到集合中，这可能会引发异常，或者可能会成功，具体取决于实现的选择。
 * <p>
 * 在此接口的规范中，此类异常被标记为“可选”。由每个集合来确定自己的同步策略。
 * 在没有更强大的实现保证的情况下，未定义的行为可能是由于在另一个线程正在改变的集合上调用任何方法；
 * 这包括直接调用、将集合传递给可能执行调用的方法以及使用现有迭代器检查集合。
 * <p>
 * Collections Framework 接口中的许多方法都是根据equals方法定义的。
 * 例如， contains(Object o)方法的规范说：“当且仅当此集合包含至少一个元素e使得(o==null ? e==null : o.equals(e))时才返回true .”
 * 本说明书不应被解释为暗示具有非空参数ö调用Collection.contains将导致o.equals（e）中被调用用于任何要素e。
 * 实现可以自由地实现优化，从而避免equals调用，例如，通过首先比较两个元素的哈希码。（ Object.hashCode()规范保证哈希码不相等的两个对象不能相等。）
 * 更一般地说，各种集合框架接口的实现可以自由地利用底层Object方法的指定行为，只要实现者认为合适.
 * 一些对集合执行递归遍历的集合操作可能会失败，但对于自引用实例（其中集合直接或间接包含自身）除外。
 * 这包括clone() 、 equals() 、 hashCode()和toString()方法。 实现可以选择性地处理自引用场景，但是大多数当前的实现都没有这样做。
 * 此接口是Java Collections Framework的成员。
 * <p>
 * Set 、 List 、 Map 、 SortedSet 、 SortedMap 、 HashSet 、 TreeSet 、 ArrayList 、 LinkedList 、 Vector 、
 * Collections 、 Arrays 、 AbstractCollection
 */
public interface CollectionMini<E> {
    //查询操作

    /**
     * 返回此集合中的元素数。 如果此集合包含多个Integer.MAX_VALUE元素，则返回Integer.MAX_VALUE
     *
     * @return 此集合中的元素数
     */
    int size();

    //判断集合是否为空
    boolean isEmpty();

    /**
     * 如果此集合包含指定的元素，则返回true 。
     * 也就是此集合包含至少一个元素e使得 o==null ? e==null || o.equals(e) 时返回true。
     * 注：也就是可能会包含多个
     */
    boolean contains(Object o);

    //把集合转换成数组
    Object[] toArray();
    // 修改操作

    /**
     * 添加元素，如果添加成功，则返回TRUE
     * 如果此集合不允许重复并且已经包含指定的元素，则返回false
     * 一些集合会拒绝添加空元素，或者对元素进行限制
     * 如果集合由于任何原因而拒绝添加特定元素，而不是因为它已经包含该元素，则它必须抛出异常（而不是返回false）
     */
    boolean add(E e);

    /**
     * 从此集合中移除指定元素的单个实例（如果存在）。
     * 和contains方法差不多，可能会一次移除多个元素
     */
    boolean remove(Object o);


    // 批量操作

    //如果此集合包含指定集合中的所有元素，则返回true 。
    boolean containsAll(@NotNull CollectionMini<?> c);

    //将指定集合中的所有元素添加到此集合
    boolean addAll(CollectionMini<? extends E> c);

    //删除也包含在指定集合中的所有此集合的元素（可选操作）。
    //调用返回后，此集合将不包含与指定集合相同的元素
    boolean removeAll(CollectionMini<?> c);


    //从该集合中移除所有未包含在指定集合中的元素，仅保留此集合中包含在指定集合中的元素。
    boolean retainAll(CollectionMini<?> c);

    //从此集合中删除所有元素（可选操作）。 此方法返回后，集合将为空。
    void clear();


    /**
     * 返回此集合中元素的迭代器。 没有关于元素返回顺序的保证（除非这个集合是提供保证的某个类的实例）
     */
    Iterator<E> iterator();

    /* 一些没啥用的东西
     * boolean equals(Object o);
     * int hashCode();
     * default Spliterator<E> spliterator() {}
     * default Stream<E> stream() {}
     * default Stream<E> parallelStream() {}
     * default boolean removeIf(Predicate<? super E> filter) {}
     */

}
