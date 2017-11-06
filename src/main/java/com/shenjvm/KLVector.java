package com.shenjvm;


public class KLVector {
    public static Object[] createAbsvector(int n) {
        return new Object[n];
    }

    public static Object[] setAbsvectorElement(Object[] ary, int idx, Object val) {
        ary[idx] = val;
        return ary;
    }

    public static Object getAbsvectorElement(Object[] ary, int idx) {
        return ary[idx];
    }
}