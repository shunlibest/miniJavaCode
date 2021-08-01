package com.collection;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * 此类提供List接口的骨架实现，以最大限度地减少实现由“随机访问”数据存储（例如数组）支持的此接口所需的工作。
 * 对于顺序访问数据（如链表），应优先使用AbstractSequentialList而非此类。
 * 要实现一个不可修改的列表，程序员只需要扩展这个类并提供get(int)和size()方法的实现。
 * 要实现可修改的列表，程序员必须另外覆盖set(int, E)方法，如果列表是可变大小的，程序员必须另外覆盖add(int, E)和remove(int)方法。
 * 根据Collection接口规范中的建议，程序员通常应提供 void（无参数）和集合构造函数。
 * 此类中每个非抽象方法的文档详细描述了其实现。 如果正在实现的集合允许更有效的实现，则可以覆盖这些方法中的每一个。
 * 此类是Java Collections Framework的成员。
 *
 * @author Josh Bloch
 * @author Neal Gafter
 */

public abstract class AbstractListMini<E> extends AbstractCollectionMini<E> implements ListMini<E> {

    protected AbstractListMini() {
    }

    //将指定的元素附加到此列表的末尾
    @Override
    public boolean add(E e) {
        add(size(), e);
        return true;
    }

    @Override
    abstract public E get(int index);

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    //它遍历列表直到找到指定的元素或到达列表的末尾，找不到时，将返回-1
    @Override
    public int indexOf(Object o) {
        ListIterator<E> it = listIterator();
        if (o == null) {
            while (it.hasNext())
                if (it.next() == null)
                    return it.previousIndex();
        } else {
            while (it.hasNext())
                if (o.equals(it.next()))
                    return it.previousIndex();
        }
        return -1;
    }

    //和上面的方法正好相反
    @Override
    public int lastIndexOf(Object o) {
        ListIterator<E> it = listIterator(size());
        if (o == null) {
            while (it.hasPrevious())
                if (it.previous() == null)
                    return it.nextIndex();
        } else {
            while (it.hasPrevious())
                if (o.equals(it.previous()))
                    return it.nextIndex();
        }
        return -1;
    }

    //从此列表中删除所有元素（可选操作）。 此调用返回后，列表将为空
    @Override
    public void clear() {
//        removeRange(0, size());
    }


    // Iterators

    //迭代器
    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    //从0开始迭代
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    /**
     * 此实现返回ListIterator接口的直接实现，它扩展了iterator()方法返回的Iterator接口的实现。
     * ListIterator实现依赖于支持列表的get(int) 、 set(int, E) 、 add(int, E)和remove(int)方法。
     * 请注意，此实现返回的列表迭代器将抛出UnsupportedOperationException以响应其remove、set和add方法，除非列表的remove(int) 、 set(int, E)和add(int, E)方法被覆盖。
     * <p>
     * modCount字段的规范中所述，此实现可以在面对并发修改时抛出运行时异常。
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ListIterator<E> listIterator(final int index) {
        rangeCheckForAdd(index);
        return new AbstractListMini.ListItr(index);
    }

    protected abstract void removeRange(int i, int i1);


    private class Itr implements Iterator<E> {
        // 当前的游标，后续调用 next 将返回的元素索引。
        int cursor = 0;

        //最近调用 next 或 previous 返回的元素索引。 如果通过调用 remove 删除此元素，则重置为 -1。
        int lastRet = -1;

        //迭代器认为支持列表应该具有的 modCount 值。 如果违反此期望，则迭代器已检测到并发修改。
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size();
        }

        public E next() {
            checkForComodification();
            try {
                int i = cursor;
                E next = get(i);
                lastRet = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                AbstractListMini.this.remove(lastRet);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public E previous() {
            checkForComodification();
            try {
                int i = cursor - 1;
                E previous = get(i);
                lastRet = cursor = i;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                AbstractListMini.this.set(lastRet, e);
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                AbstractListMini.this.add(i, e);
                lastRet = -1;
                cursor = i + 1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

//    /**
//     * {@inheritDoc}
//     *
//     * <p>This implementation returns a list that subclasses
//     * {@code AbstractList}.  The subclass stores, in private fields, the
//     * offset of the subList within the backing list, the size of the subList
//     * (which can change over its lifetime), and the expected
//     * {@code modCount} value of the backing list.  There are two variants
//     * of the subclass, one of which implements {@code RandomAccess}.
//     * If this list implements {@code RandomAccess} the returned list will
//     * be an instance of the subclass that implements {@code RandomAccess}.
//     *
//     * <p>The subclass's {@code set(int, E)}, {@code get(int)},
//     * {@code add(int, E)}, {@code remove(int)}, {@code addAll(int,
//     * Collection)} and {@code removeRange(int, int)} methods all
//     * delegate to the corresponding methods on the backing abstract list,
//     * after bounds-checking the index and adjusting for the offset.  The
//     * {@code addAll(Collection c)} method merely returns {@code addAll(size,
//     * c)}.
//     *
//     * <p>The {@code listIterator(int)} method returns a "wrapper object"
//     * over a list iterator on the backing list, which is created with the
//     * corresponding method on the backing list.  The {@code iterator} method
//     * merely returns {@code listIterator()}, and the {@code size} method
//     * merely returns the subclass's {@code size} field.
//     *
//     * <p>All methods first check to see if the actual {@code modCount} of
//     * the backing list is equal to its expected value, and throw a
//     * {@code ConcurrentModificationException} if it is not.
//     *
//     * @throws IndexOutOfBoundsException if an endpoint index value is out of range
//     *                                   {@code (fromIndex < 0 || toIndex > size)}
//     * @throws IllegalArgumentException  if the endpoint indices are out of order
//     *                                   {@code (fromIndex > toIndex)}
//     */
//    public ListMini<E> subList(int fromIndex, int toIndex) {
//        return (this instanceof RandomAccess ?
//                new RandomAccessSubList<>(this, fromIndex, toIndex) :
//                new SubList<>(this, fromIndex, toIndex));
//    }

    //
//    /**
//     * Removes from this list all of the elements whose index is between
//     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.
//     * Shifts any succeeding elements to the left (reduces their index).
//     * This call shortens the list by {@code (toIndex - fromIndex)} elements.
//     * (If {@code toIndex==fromIndex}, this operation has no effect.)
//     *
//     * <p>This method is called by the {@code clear} operation on this list
//     * and its subLists.  Overriding this method to take advantage of
//     * the internals of the list implementation can <i>substantially</i>
//     * improve the performance of the {@code clear} operation on this list
//     * and its subLists.
//     *
//     * <p>This implementation gets a list iterator positioned before
//     * {@code fromIndex}, and repeatedly calls {@code ListIterator.next}
//     * followed by {@code ListIterator.remove} until the entire range has
//     * been removed.  <b>Note: if {@code ListIterator.remove} requires linear
//     * time, this implementation requires quadratic time.</b>
//     *
//     * @param fromIndex index of first element to be removed
//     * @param toIndex   index after last element to be removed
//     */
//    protected void removeRange(int fromIndex, int toIndex) {
//        ListIterator<E> it = listIterator(fromIndex);
//        for (int i = 0, n = toIndex - fromIndex; i < n; i++) {
//            it.next();
//            it.remove();
//        }
//    }
//
//    /**
//     * The number of times this list has been <i>structurally modified</i>.
//     * Structural modifications are those that change the size of the
//     * list, or otherwise perturb it in such a fashion that iterations in
//     * progress may yield incorrect results.
//     *
//     * <p>This field is used by the iterator and list iterator implementation
//     * returned by the {@code iterator} and {@code listIterator} methods.
//     * If the value of this field changes unexpectedly, the iterator (or list
//     * iterator) will throw a {@code ConcurrentModificationException} in
//     * response to the {@code next}, {@code remove}, {@code previous},
//     * {@code set} or {@code add} operations.  This provides
//     * <i>fail-fast</i> behavior, rather than non-deterministic behavior in
//     * the face of concurrent modification during iteration.
//     *
//     * <p><b>Use of this field by subclasses is optional.</b> If a subclass
//     * wishes to provide fail-fast iterators (and list iterators), then it
//     * merely has to increment this field in its {@code add(int, E)} and
//     * {@code remove(int)} methods (and any other methods that it overrides
//     * that result in structural modifications to the list).  A single call to
//     * {@code add(int, E)} or {@code remove(int)} must add no more than
//     * one to this field, or the iterators (and list iterators) will throw
//     * bogus {@code ConcurrentModificationExceptions}.  If an implementation
//     * does not wish to provide fail-fast iterators, this field may be
//     * ignored.
//     */
    protected transient int modCount = 0;

    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size();
    }
}

