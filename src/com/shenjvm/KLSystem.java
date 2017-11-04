package com.shenjvm;

public class KLSystem {
    public static final long startNanoTime = System.nanoTime();

    public static long getUnixTime() {
        return System.currentTimeMillis() / 1000L;
    }

    public static double getRunTime() {
        return (System.nanoTime() - startNanoTime) / 1e9;
    }

    public static Number getTime(KLSymbol type) {
        if (type == KLSymbolPool.UNIX)
            return getUnixTime();
        else if (type == KLSymbolPool.RUN)
            return getRunTime();
        throw new KLException("Argument of get-time should be a symbol unix or run");
    }
}