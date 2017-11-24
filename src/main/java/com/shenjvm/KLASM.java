package com.shenjvm;

import java.util.ArrayList;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

public class KLASM {
    public static final Type ARRAY_LIST_TYPE = Type.getType(ArrayList.class);
    public static final Type CLASS_TYPE = Type.getType(Class.class);
    public static final Type DOUBLE_TYPE = Type.getType(Double.class);
    public static final Type EXCEPTION_TYPE = Type.getType(Exception.class);
    public static final Type LONG_TYPE = Type.getType(Long.class);
    public static final Type NUMBER_TYPE = Type.getType(Number.class);
    public static final Type OBJECT_ARRAY_TYPE = Type.getType(Object[].class);
    public static final Type OBJECT_TYPE = Type.getType(Object.class);
    public static final Type REFLECT_METHOD_TYPE = Type.getType(java.lang.reflect.Method.class);
    public static final Type STRING_TYPE = Type.getType(String.class);

    public static final Type VOID_TYPE = Type.getType(Void.class);

    public static final Type ASM_TYPE_TYPE = Type.getType(Type.class);

    public static final Type KL_TYPE = Type.getType(KL.class);
    public static final Type KLASM_TYPE = Type.getType(KLASM.class);
    public static final Type KLACLOSEDENV_TYPE = Type.getType(KLAClosedEnv.class);
    public static final Type KLCONS_TYPE = Type.getType(KLCons.class);
    public static final Type KLEXCEPTION_TYPE = Type.getType(KLException.class);
    public static final Type KLFUNCTION_TYPE = Type.getType(KLFunction.class);
    public static final Type KLINIT_TYPE = Type.getType(KLInit.class);
    public static final Type KLISTREAM_TYPE = Type.getType(KLIStream.class);
    public static final Type KLLAMBDA_TYPE = Type.getType(KLLambda.class);
    public static final Type KLPRIMITIVE_TYPE = Type.getType(KLPrimitive.class);
    public static final Type KLSYMBOL_TYPE = Type.getType(KLSymbol.class);
    public static final Type KLSYMBOLPOOL_TYPE = Type.getType(KLSymbolPool.class);


    public static void invokeIntern(GeneratorAdapter ga) {
        ga.invokeStatic(KLPRIMITIVE_TYPE, KLMethodPool.internMethod);
    }

    public static void intern(GeneratorAdapter ga, String s) {
        ga.push(s);
        invokeIntern(ga);
    }

    public static void intern(GeneratorAdapter ga, KLSymbol sym) {
        ga.push(sym.name);
        invokeIntern(ga);
    }

    public static void intern(GeneratorAdapter ga, Object o) {
        ga.push(((KLSymbol)o).name);
        invokeIntern(ga);
    }

    public static void invokeLongValueOf(GeneratorAdapter ga) {
        ga.invokeStatic(LONG_TYPE, KLMethodPool.longValueOfMethod);
    }

    public static void invokeDoubleValueOf(GeneratorAdapter ga) {
        ga.invokeStatic(DOUBLE_TYPE, KLMethodPool.doubleValueOfMethod);
    }

    public static void boxLong(GeneratorAdapter ga, long x) {
        ga.push(x);
        invokeLongValueOf(ga);
    }

    public static void boxLong(GeneratorAdapter ga, Object o) {
        ga.push((Long)o);
        invokeLongValueOf(ga);
    }

    public static void boxDouble(GeneratorAdapter ga, double x) {
        ga.push(x);
        invokeDoubleValueOf(ga);
    }

    public static void boxDouble(GeneratorAdapter ga, Object o) {
        ga.push((Double)o);
        invokeDoubleValueOf(ga);
    }
}