package com.shenjvm;

public class KLStream {
    public static void writeByte(int x, KLBufferedWriter stm) {
        stm.writeByte(x);
    }

    public static int readByte(KLBufferedReader stm) {
        return stm.readByte();
    }

    public static boolean isStream(KLIStream stm) {
        return true;
    }

    public static boolean isStream(Object x) {
        return x instanceof KLIStream;
    }
}