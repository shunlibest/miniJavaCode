package com.collection;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

class ArrayListMiniTest {
    ArrayListMini<String> arrayListMini = new ArrayListMini<>();
    ArrayList<String> arrayList = new ArrayList<>();


    @Test
    void trimToSize() {
    }

    @Test
    void ensureCapacity() {
    }

    @Test
    void size()  {
        print();
        addValues(1);
        print();

        addValues(10);
        print();

        arrayListMini.clear();
        print();

        arrayListMini.trimToSize();
        print();

        addValues(1);
        print();

        addValues(2);
        print();

        addValues(4);
        print();
    }

    void print(){
        System.out.print("arrayListMini元素个数:" + arrayListMini.size());
        System.out.println(" 存储空间为:" + arrayListMini.getCapacitySize());
    }


    void addValues(int size) {
        for (int i = 0; i < size; i++) {
            arrayListMini.add("" + i);
            arrayList.add("" + i);
        }
    }
    private int getCapacitySize(ArrayList<String> arrayList){
        try {
            Class<ArrayList<String>> pClass = (Class<ArrayList<String>>) arrayList.getClass();
            Field elementData = pClass.getDeclaredField("elementData");
            elementData.setAccessible(true); // 为true时可以访问私有类型变量
            Object[] o = (Object[]) elementData.get(arrayList);
            return o.length;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
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