package com.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

/**
 * List接口的可调整大小的数组实现。实现所有可选的列表操作，并允许所有元素，包括null。
 * 除了实现List接口之外，该类还提供了操作内部用于存储列表的数组大小的方法。（这个类大致相当于Vector ，只是它是不同步的。）
 * size 、 isEmpty 、 get 、 set 、 iterator和listIterator操作在恒定时间内运行。
 * add操作在分摊常数 time 内运行，即添加 n 个元素需要 O(n) 时间。 所有其他操作都在线性时间内运行，与LinkedList实现相比，常量因子较低。
 * <p>
 * 每个ArrayList实例都有一个capacity，容量是用于存储列表中元素的数组的大小，大于等于集合内元素
 * 随着元素被添加到ArrayList，它的容量会自动增长。除了添加元素具有恒定的摊销时间成本之外，没有指定增长政策的细节。
 * 应用程序可以在使用ensureCapacity操作添加大量元素之前增加ArrayList实例的容量。 这可以减少增量重新分配的量。
 * 请注意，此实现不是同步的。 如果多个线程同时访问一个ArrayList实例，并且至少有一个线程在结构上修改了列表，则必须在外部进行同步。
 * （结构修改是添加或删除一个或多个元素，或显式调整后备数组大小的任何操作；仅设置元素的值不是结构修改。）这通常是通过同步一些自然封装的对象来完成的列表。
 * 如果不存在此类对象，则应使用Collections.synchronizedList方法“包装”该列表。 这最好在创建时完成，以防止对列表的意外不同步访问：
 * List list = Collections.synchronizedList(new ArrayList(...));
 * 此类的iterator和listIterator方法返回的iterator是快速失败的：
 * 如果在创建迭代器后的任何时间以任何方式修改了列表的结构，除了通过迭代器自己的remove或add方法，迭代器将抛出ConcurrentModificationException 。
 * 因此，面对并发修改，迭代器快速而干净地失败，而不是在未来不确定的时间冒着任意、非确定性行为的风险。
 * 请注意，无法保证迭代器的快速失败行为，因为一般而言，在存在非同步并发修改的情况下不可能做出任何硬保证。
 * 快速失败的迭代器会尽最大努力抛出ConcurrentModificationException 。
 * 因此，编写一个依赖此异常来确保其正确性的程序是错误的：迭代器的快速失败行为应该仅用于检测错误。
 */

public class ArrayListMini<E> extends AbstractListMini<E> implements ListMini<E> {

    //默认初始容量。
    private static final int DEFAULT_CAPACITY = 10;

    //用于空实例的共享空数组实例
    private static final Object[] EMPTY_ELEMENTDATA = {};

    //用于默认大小的空实例的共享空数组实例。
    // 我们将其与 EMPTY_ELEMENTDATA 区分开来，以了解添加第一个元素时要膨胀多少
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    //ArrayList的元素存储在其中的数组缓冲区。
    //ArrayList 的容量就是这个数组缓冲区的长度。
    //添加第一个元素时，任何带有 elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA 的空 ArrayList 都将扩展为 DEFAULT_CAPACITY。
    transient Object[] elementData;

    //ArrayList 的大小，它包含的元素数。
    private int size;

    //构造一个具有指定初始容量的空列表
    public ArrayListMini(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " +
                    initialCapacity);
        }
    }

    // 构造一个初始容量为 10 的空列表。
    public ArrayListMini() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    //将此ArrayList容量空间 减小 为列表的当前真正的大小。
    public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            elementData = (size == 0)
                    ? EMPTY_ELEMENTDATA
                    : Arrays.copyOf(elementData, size);
        }
    }

    //增加此ArrayList实例的容量，以确保它至少可以容纳由最小容量参数指定的元素数量。
    public void ensureCapacity(int minCapacity) {
        int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
                // 任何大小，如果不是默认元素表
                ? 0
                // 大于默认空表的默认值。它已经应该是默认大小。
                : DEFAULT_CAPACITY;

        if (minCapacity > minExpand) {
            ensureExplicitCapacity(minCapacity);
        }
    }

    //额外增加的方法，获取存储空间大小
    public int getCapacitySize() {
        return elementData.length;
    }

    private static int calculateCapacity(Object[] elementData, int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        return minCapacity;
    }

    //minCapacity, 最小需要的存储空间
    private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }

    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;
        //当前的存储空间，小于所需要的最小空间，执行扩容机制
        if (elementData.length < minCapacity)
            grow(minCapacity);
    }

    //要分配的数组的最大大小
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    //增加容量以确保它至少可以容纳由最小容量参数指定的元素数量。
    //集合第一次add元素时，会扩容，这时会比较 0*1.5 和10 的大小
    private void grow(int minCapacity) {
        // 未扩容前的存储空间大小
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        //取一个合适的空间大小，正常情况下，扩容到1.5倍，如果依然小于所需存储空间，那么直接就把空间改为最小空间。
        newCapacity = Math.max(newCapacity, minCapacity);
        //这里各种防止溢出
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        //拷贝，生产一个新的存储空间
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    //这里重写了，使用了数组循环，而不是迭代器
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i] == null)
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--)
                if (elementData[i] == null)
                    return i;
        } else {
            for (int i = size - 1; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    @Override
    public ListMini<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ArrayListMini<?> v = (ArrayListMini<?>) super.clone();
        v.elementData = Arrays.copyOf(elementData, size);
        v.modCount = 0;
        return v;
    }

    //此方法必须分配一个新数组）。 因此调用者可以自由地修改返回的数组。
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }


    private E elementData(int index) {
        return (E) elementData[index];
    }

    //返回此列表中指定位置的元素
    @Override
    public E get(int index) {
        rangeCheck(index);
        return elementData(index);
    }

    //用指定的元素替换此列表中指定位置的元素。
    @Override
    public E set(int index, E element) {
        rangeCheck(index);
        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    //将指定的元素附加到此列表的末尾。
    @Override
    public boolean add(E e) {
        //增加 modCount ！！
        ensureCapacityInternal(size + 1);
        elementData[size++] = e;
        return true;
    }

    //在此列表中的指定位置插入指定元素，后续元素向后移动一位
    @Override
    public void add(int index, E element) {
        rangeCheck(index);
        ensureCapacityInternal(size + 1);
        //把索引后面数组元素往后复制一位
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        size++;
    }

    //移除一个元素，并且返回值将会是移除的元素
    @Override
    public E remove(int index) {
        rangeCheck(index);
        //这里不需要ensureCapacityInternal，所以就要写modCount
        modCount++;
        E oldValue = elementData(index);

        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index + 1, elementData, index,
                    numMoved);
        elementData[--size] = null;
        return oldValue;
    }

    //跳过边界检查并且不返回移除的值的私有移除方法。
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index + 1, elementData, index,
                    numMoved);
        elementData[--size] = null; // clear to let GC do its work
    }


    //从此列表中删除第一次出现的指定元素（如果存在
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }


    //从此列表中删除所有元素。此调用返回后，列表将为空。
    @Override
    public void clear() {
        modCount++;
        for (int i = 0; i < size; i++)
            elementData[i] = null;
        size = 0;
    }

    //将指定集合中的所有元素追加到此列表的末尾
    @Override
    public boolean addAll(CollectionMini<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        return numNew != 0;
    }

    //从指定位置开始，将指定集合中的所有元素插入此列表
    public boolean addAll(int index, CollectionMini<? extends E> c) {
        rangeCheck(index);
        Object[] a = c.toArray();
        int numNew = a.length;
        // Increments modCount
        ensureCapacityInternal(size + numNew);

        int numMoved = size - index;
        //索引位置后面的元素，往后挪numMoved个元素，留给新来的元素
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                    numMoved);
        //把新来的复制到对应的位置
        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return numNew != 0;
    }

    //从此列表中删除其索引介于fromIndex和toIndex之间（不包括在内）的所有元素
    protected void removeRange(int fromIndex, int toIndex) {
        modCount++;
        int numMoved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex, numMoved);
        int newSize = size - (toIndex - fromIndex);
        for (int i = newSize; i < size; i++) {
            elementData[i] = null;
        }
        size = newSize;
    }

    //检查给定的索引是否在范围内
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    //从此列表中删除包含在指定集合中的所有元素。
    public boolean removeAll(@NotNull CollectionMini<?> c) {
        return batchRemove(c, false);
    }

    //仅保留此列表中包含在指定集合中的元素
    public boolean retainAll(@NotNull CollectionMini<?> c) {
        return batchRemove(c, true);
    }

    private boolean batchRemove(CollectionMini<?> c, boolean complement) {
        final Object[] elementData = this.elementData;
        int r = 0, w = 0;
        boolean modified = false;
        try {
            for (; r < size; r++)
                //判断是保留下俩，还是删除
                if (c.contains(elementData[r]) == complement)
                    elementData[w++] = elementData[r];
        } finally {
            // 保持与 AbstractCollection 的行为兼容性，即使 c.contains() 抛出。
            //TODO 没看懂，什么情况下会发生 r != size
            if (r != size) {
                System.arraycopy(elementData, r, elementData, w, size - r);
                w += size - r;
            }
            if (w != size) {
                for (int i = w; i < size; i++)
                    elementData[i] = null;
                modCount += size - w;
                size = w;
                modified = true;
            }
        }
        return modified;
    }


    //private void writeObject(java.io.ObjectOutputStream s) {}
    //private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {}

    //返回此列表中元素的列表迭代器（以适当的顺序
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index);
        return new ListItr(index);
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * An optimized version of AbstractList.Itr
     */
    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;

        Itr() {
        }

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = ArrayListMini.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayListMini.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    /**
     * An optimized version of AbstractList.ListItr
     */
    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        @SuppressWarnings("unchecked")
        public E previous() {
            checkForComodification();
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = ArrayListMini.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayListMini.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                ArrayListMini.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

//
//    public ListMini<E> subList(int fromIndex, int toIndex) {
//        subListRangeCheck(fromIndex, toIndex, size);
//        return new SubList(this, 0, fromIndex, toIndex);
//    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                    ") > toIndex(" + toIndex + ")");
    }

    private class SubList extends AbstractListMini<E> implements RandomAccess {
        private final AbstractListMini<E> parent;
        private final int parentOffset;
        private final int offset;
        int size;

        SubList(AbstractListMini<E> parent,
                int offset, int fromIndex, int toIndex) {
            this.parent = parent;
            this.parentOffset = fromIndex;
            this.offset = offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = ArrayListMini.this.modCount;
        }

        public E set(int index, E e) {
            rangeCheck(index);
            checkForComodification();
            E oldValue = ArrayListMini.this.elementData(offset + index);
            ArrayListMini.this.elementData[offset + index] = e;
            return oldValue;
        }

        public E get(int index) {
            rangeCheck(index);
            checkForComodification();
            return ArrayListMini.this.elementData(offset + index);
        }

        public int size() {
            checkForComodification();
            return this.size;
        }

        public void add(int index, E e) {
            rangeCheckForAdd(index);
            checkForComodification();
            parent.add(parentOffset + index, e);
            this.modCount = parent.modCount;
            this.size++;
        }

        public E remove(int index) {
            rangeCheck(index);
            checkForComodification();
            E result = parent.remove(parentOffset + index);
            this.modCount = parent.modCount;
            this.size--;
            return result;
        }

        protected void removeRange(int fromIndex, int toIndex) {
            checkForComodification();
            parent.removeRange(parentOffset + fromIndex,
                    parentOffset + toIndex);
            this.modCount = parent.modCount;
            this.size -= toIndex - fromIndex;
        }

        public boolean addAll(CollectionMini<? extends E> c) {
            return addAll(this.size, c);
        }

        public boolean addAll(int index, CollectionMini<? extends E> c) {
            rangeCheckForAdd(index);
            int cSize = c.size();
            if (cSize == 0)
                return false;

            checkForComodification();
//            parent.addAll(parentOffset + index, c);
            this.modCount = parent.modCount;
            this.size += cSize;
            return true;
        }

        public Iterator<E> iterator() {
            return listIterator();
        }

        public ListIterator<E> listIterator(final int index) {
            checkForComodification();
            rangeCheckForAdd(index);
            final int offset = this.offset;

            return new ListIterator<E>() {
                int cursor = index;
                int lastRet = -1;
                int expectedModCount = ArrayListMini.this.modCount;

                public boolean hasNext() {
                    return cursor != SubList.this.size;
                }

                @SuppressWarnings("unchecked")
                public E next() {
                    checkForComodification();
                    int i = cursor;
                    if (i >= SubList.this.size)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayListMini.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i + 1;
                    return (E) elementData[offset + (lastRet = i)];
                }

                public boolean hasPrevious() {
                    return cursor != 0;
                }

                @SuppressWarnings("unchecked")
                public E previous() {
                    checkForComodification();
                    int i = cursor - 1;
                    if (i < 0)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayListMini.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i;
                    return (E) elementData[offset + (lastRet = i)];
                }

                @SuppressWarnings("unchecked")
                public void forEachRemaining(Consumer<? super E> consumer) {
                    Objects.requireNonNull(consumer);
                    final int size = SubList.this.size;
                    int i = cursor;
                    if (i >= size) {
                        return;
                    }
                    final Object[] elementData = ArrayListMini.this.elementData;
                    if (offset + i >= elementData.length) {
                        throw new ConcurrentModificationException();
                    }
                    while (i != size && modCount == expectedModCount) {
                        consumer.accept((E) elementData[offset + (i++)]);
                    }
                    // update once at end of iteration to reduce heap write traffic
                    lastRet = cursor = i;
                    checkForComodification();
                }

                public int nextIndex() {
                    return cursor;
                }

                public int previousIndex() {
                    return cursor - 1;
                }

                public void remove() {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        SubList.this.remove(lastRet);
                        cursor = lastRet;
                        lastRet = -1;
                        expectedModCount = ArrayListMini.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void set(E e) {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        ArrayListMini.this.set(offset + lastRet, e);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void add(E e) {
                    checkForComodification();

                    try {
                        int i = cursor;
                        SubList.this.add(i, e);
                        cursor = i + 1;
                        lastRet = -1;
                        expectedModCount = ArrayListMini.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                final void checkForComodification() {
                    if (expectedModCount != ArrayListMini.this.modCount)
                        throw new ConcurrentModificationException();
                }
            };
        }

        public ListMini<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new SubList(this, offset, fromIndex, toIndex);
        }

        private void rangeCheck(int index) {
            if (index < 0 || index >= this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private String outOfBoundsMsg(int index) {
            return "Index: " + index + ", Size: " + this.size;
        }

        private void checkForComodification() {
            if (ArrayListMini.this.modCount != this.modCount)
                throw new ConcurrentModificationException();
        }
    }


    @Override
    public void sort(Comparator<? super E> c) {
        final int expectedModCount = modCount;
        Arrays.sort((E[]) elementData, 0, size, c);
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }
}
