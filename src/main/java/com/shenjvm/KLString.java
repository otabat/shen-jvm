package com.shenjvm;

public class KLString {
    public static String get(String s, int idx) {
        return s.substring(idx, idx + 1);
    }

    public static String nToString(int codePoint) {
        return String.valueOf(((char)codePoint));
    }

    public static int stringToN(String s) {
        return s.codePointAt(0);
    }
}
