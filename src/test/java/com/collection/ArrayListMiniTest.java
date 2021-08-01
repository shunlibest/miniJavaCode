package com.collection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

class ArrayListMiniTest {
    ArrayListMini<String> arrayListMini = new ArrayListMini<>(9);
    ArrayList<String> arrayList = new ArrayList<>();

    void initValues(int size) {
        arrayListMini.clear();
        arrayList.clear();
        for (int i = 0; i < size; i++) {
            arrayListMini.add("" + i);
            arrayList.add("" + i);
        }
    }

    @Test
    void trimToSize() {
    }

    @Test
    void ensureCapacity() {
    }

    @Test
    void size() throws NoSuchFieldException, IllegalAccessException {
        initValues(11);
        System.out.print("arrayListMini元素个数 " + arrayListMini.size());
        System.out.println("arrayListMini存储空间为 " + arrayListMini.getCapacitySize());
        System.out.print("arrayList元素个数 " + arrayList.size());
        Class<ArrayList<String>> pClass = (Class<ArrayList<String>>) arrayList.getClass();
        Field elementData = pClass.getDeclaredField("elementData");
        elementData.setAccessible(true); // 为true时可以访问私有类型变量
        Object[] o = (Object[]) elementData.get(arrayList);
        System.out.println("arrayListMini存储空间为 " + o.length);
    }

    @Test
    void isEmpty() {
    }

    @Test
    void contains() {
    }

    @Test
    void indexOf() {
    }

    @Test
    void toArray() {
    }

    @Test
    void get() {
    }

    @Test
    void set() {
    }

    @Test
    void add() {
    }

    @Test
    void testAdd() {
    }

    @Test
    void remove() {
    }

    @Test
    void clear() {
    }

    @Test
    void addAll() {
    }

    @Test
    void sort() {
    }
}