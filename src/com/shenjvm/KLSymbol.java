package com.shenjvm;

import java.util.HashMap;

public class KLSymbol {
    public final String name;
    public Object varValue;
    public KLFunction fun;
    public static HashMap<String, KLSymbol> symNameToSymMap = new HashMap<String, KLSymbol>();

    KLSymbol(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static KLFunction getFun(KLSymbol sym) {
        return sym.fun;
    }

    public static KLFunction getFun(Object sym) {
        return ((KLSymbol)sym).fun;
    }

    public static void setFun(KLSymbol sym, KLFunction fun) {
        sym.fun = fun;
    }
}
