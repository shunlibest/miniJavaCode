package com.map;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * 将键映射到值的对象。Map不能包含重复的键； 每个键最多可以映射到一个值。
 * 这个接口取代了Dictionary类，它是一个完全抽象的类，而不是一个接口。
 * <p>
 * Map接口提供了三个集合视图，允许将地图的内容视为一组键、一组值或一组键值映射。
 * 一些Map，如TreeMap类，对它们的顺序做出特定保证； 其他的，比如HashMap类，则没有。
 * 注意：如果将可变对象用作映射键，则必须非常小心。 如果对象的值以影响等于比较的方式更改，而对象是映射中的键，则不会指定映射的行为。
 * 此禁令的一个特殊情况是不允许映射将自身包含为键。 虽然允许映射将自身包含为值，但建议格外小心：在此类映射上不再明确定义equals和hashCode方法。
 * <p>
 * 此接口中包含的“破坏性”方法，即修改它们操作的映射的方法，如果此映射不支持该操作，则指定为抛出UnsupportedOperationException 。
 * 在这种情况下，如果调用对地图没有影响，则这些方法可能（但不是必需）抛出UnsupportedOperationException 。
 * 例如，如果要“叠加”映射的映射为空，则在不可修改的映射上调用putAll(Map)方法可能（但不是必需）引发异常。
 * <p>
 * 一些地图实现对它们可能包含的键和值有限制。 例如，有些实现禁止空键和值，有些实现对其键的类型有限制。
 * <p>
 * Collections Framework 接口中的许多方法都是根据equals方法定义的。
 * 例如，对于在本说明书containsKey(Object key)方法表示：“返回true当且仅当此映射包含一个密钥k，
 * 使得（键== NULL的映射满足K == NULL：？key.equals（k）的） 。”
 * 本说明书不应被解释为暗示具有非空参数密钥调用Map.containsKey将导致key.equals（k）输出到被调用任何密钥k。
 * 实现可以自由地实现优化，从而避免equals调用，例如，通过首先比较两个键的哈希码。（ Object.hashCode()规范保证哈希码不相等的两个对象不能相等。）
 * 更一般地说，各种集合框架接口的实现可以自由地利用底层Object方法的指定行为，只要实现者认为合适.
 */
public interface MapMini<K, V> {
    // 查询操作

    int size();

    boolean isEmpty();

    boolean containsKey(Object key);

    //如果此映射将一个或多个键映射到指定值，则返回true
    boolean containsValue(Object value);

    //返回指定键映射到的值，如果此映射不包含键的映射，则返回null 。
    V get(Object key);

    // 修改操作

    //将指定值与此映射中的指定键相关联。 如果映射先前包含键的映射，则旧值将替换为指定值
    V put(K key, V value);

    //如果此映射包含从键k到值v的映射，使得(key==null ? k==null : key.equals(k)) ，则删除该映射。
    // Map中最多可以包含一个这样的映射
    V remove(Object key);


    // Bulk Operations

    //此调用的效果等同于对指定映射中从键k到值v 的每个映射在此映射上调用一次put(k, v)
    void putAll(MapMini<? extends K, ? extends V> m);

    //从此映射中删除所有映射（可选操作）。 此调用返回后，地图将为空。
    void clear();


    // Views

    //返回此映射中包含的键的Set视图。
    // 如果在对Map迭代时修改了，则迭代的结果是不确定的。
    // 该集合支持元素移除，即通过Iterator.remove 、 Set.remove 、 removeAll 、 retainAll和clear操作从地图中移除相应的映射。
    // 它不支持add或addAll操作
    Set<K> keySet();

    //返回此映射中包含的值的Collection视图。
    //要求和上面一样
    Collection<V> values();

    //返回此映射中包含的映射的Set视图
    Set<Entry<K, V>> entrySet();

    /**
     * 一个映射条目（键值对）。 Map.entrySet方法返回地图的集合视图，其元素属于此类。
     * 获取映射条目引用的唯一方法是从此集合视图的迭代器中获取。
     * 这些Map.Entry对象仅在迭代期间有效； 更正式地说，如果在迭代器返回条目后修改了后备映射，则映射条目的行为是未定义的，除非通过对映射条目的setValue操作
     */
    interface Entry<K, V> {
        /**
         * Returns the key corresponding to this entry.
         *
         * @return the key corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *                               required to, throw this exception if the entry has been
         *                               removed from the backing map.
         */
        K getKey();

        /**
         * Returns the value corresponding to this entry.  If the mapping
         * has been removed from the backing map (by the iterator's
         * <tt>remove</tt> operation), the results of this call are undefined.
         *
         * @return the value corresponding to this entry
         * @throws IllegalStateException implementations may, but are not
         *                               required to, throw this exception if the entry has been
         *                               removed from the backing map.
         */
        V getValue();

        /**
         * Replaces the value corresponding to this entry with the specified
         * value (optional operation).  (Writes through to the map.)  The
         * behavior of this call is undefined if the mapping has already been
         * removed from the map (by the iterator's <tt>remove</tt> operation).
         *
         * @param value new value to be stored in this entry
         * @return old value corresponding to the entry
         * @throws UnsupportedOperationException if the <tt>put</tt> operation
         *                                       is not supported by the backing map
         * @throws ClassCastException            if the class of the specified value
         *                                       prevents it from being stored in the backing map
         * @throws NullPointerException          if the backing map does not permit
         *                                       null values, and the specified value is null
         * @throws IllegalArgumentException      if some property of this value
         *                                       prevents it from being stored in the backing map
         * @throws IllegalStateException         implementations may, but are not
         *                                       required to, throw this exception if the entry has been
         *                                       removed from the backing map.
         */
        V setValue(V value);

        /**
         * Compares the specified object with this entry for equality.
         * Returns <tt>true</tt> if the given object is also a map entry and
         * the two entries represent the same mapping.  More formally, two
         * entries <tt>e1</tt> and <tt>e2</tt> represent the same mapping
         * if<pre>
         *     (e1.getKey()==null ?
         *      e2.getKey()==null : e1.getKey().equals(e2.getKey()))  &amp;&amp;
         *     (e1.getValue()==null ?
         *      e2.getValue()==null : e1.getValue().equals(e2.getValue()))
         * </pre>
         * This ensures that the <tt>equals</tt> method works properly across
         * different implementations of the <tt>Map.Entry</tt> interface.
         *
         * @param o object to be compared for equality with this map entry
         * @return <tt>true</tt> if the specified object is equal to this map
         * entry
         */
        boolean equals(Object o);

        /**
         * Returns the hash code value for this map entry.  The hash code
         * of a map entry <tt>e</tt> is defined to be: <pre>
         *     (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
         *     (e.getValue()==null ? 0 : e.getValue().hashCode())
         * </pre>
         * This ensures that <tt>e1.equals(e2)</tt> implies that
         * <tt>e1.hashCode()==e2.hashCode()</tt> for any two Entries
         * <tt>e1</tt> and <tt>e2</tt>, as required by the general
         * contract of <tt>Object.hashCode</tt>.
         *
         * @return the hash code value for this map entry
         * @see Object#hashCode()
         * @see Object#equals(Object)
         * @see #equals(Object)
         */
        int hashCode();

        /**
         * Returns a comparator that compares {@link Entry} in natural order on key.
         *
         * <p>The returned comparator is serializable and throws {@link
         * NullPointerException} when comparing an entry with a null key.
         *
         * @param <K> the {@link Comparable} type of then map keys
         * @param <V> the type of the map values
         * @return a comparator that compares {@link Entry} in natural order on key.
         * @see Comparable
         * @since 1.8
         */
        public static <K extends Comparable<? super K>, V> Comparator<Entry<K, V>> comparingByKey() {
            return (Comparator<Entry<K, V>> & Serializable)
                    (c1, c2) -> c1.getKey().compareTo(c2.getKey());
        }

        /**
         * Returns a comparator that compares {@link Entry} in natural order on value.
         *
         * <p>The returned comparator is serializable and throws {@link
         * NullPointerException} when comparing an entry with null values.
         *
         * @param <K> the type of the map keys
         * @param <V> the {@link Comparable} type of the map values
         * @return a comparator that compares {@link Entry} in natural order on value.
         * @see Comparable
         * @since 1.8
         */
        public static <K, V extends Comparable<? super V>> Comparator<Entry<K, V>> comparingByValue() {
            return (Comparator<Entry<K, V>> & Serializable)
                    (c1, c2) -> c1.getValue().compareTo(c2.getValue());
        }

        /**
         * Returns a comparator that compares {@link Entry} by key using the given
         * {@link Comparator}.
         *
         * <p>The returned comparator is serializable if the specified comparator
         * is also serializable.
         *
         * @param <K> the type of the map keys
         * @param <V> the type of the map values
         * @param cmp the key {@link Comparator}
         * @return a comparator that compares {@link Entry} by the key.
         * @since 1.8
         */
        public static <K, V> Comparator<Entry<K, V>> comparingByKey(Comparator<? super K> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<Entry<K, V>> & Serializable)
                    (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
        }

        /**
         * Returns a comparator that compares {@link Entry} by value using the given
         * {@link Comparator}.
         *
         * <p>The returned comparator is serializable if the specified comparator
         * is also serializable.
         *
         * @param <K> the type of the map keys
         * @param <V> the type of the map values
         * @param cmp the value {@link Comparator}
         * @return a comparator that compares {@link Entry} by the value.
         * @since 1.8
         */
        public static <K, V> Comparator<Entry<K, V>> comparingByValue(Comparator<? super V> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<Entry<K, V>> & Serializable)
                    (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
        }
    }

    // 比较和散列

//    boolean equals(Object o);
//    int hashCode();

    // 下面都是一些对Map的工具方法，可以不用看

    //返回指定键映射到的值，如果此映射不包含键的映射，则返回defaultValue 。
    default V getOrDefault(Object key, V defaultValue) {
        V v;
        return (((v = get(key)) != null) || containsKey(key)) ? v : defaultValue;
    }

    //对此映射中的每个条目执行给定的操作，直到处理完所有条目或该操作引发异常。
    // 除非实现类另有规定，否则将按照条目集迭代的顺序执行操作（如果指定了迭代顺序）。操作抛出的异常将转发给调用者
    default void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (Entry<K, V> entry : entrySet()) {
            K k;
            V v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }
//    default void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {}
//    default V putIfAbsent(K key, V value) {}
//    仅当当前映射到指定值时，才删除指定键的条目。
//    default boolean remove(Object key, Object value) {}
//    default boolean replace(K key, V oldValue, V newValue) {}
//    default V replace(K key, V value) {}
//    default V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {}
//    default V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {}//   default V compute(K key,BiFunction<? super K, ? super V, ? extends V> remappingFunction) {}
//    default V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {}
}
