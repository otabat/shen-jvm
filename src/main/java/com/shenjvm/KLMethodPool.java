package com.shenjvm;

import org.objectweb.asm.commons.Method;

public class KLMethodPool {
    public static final Method internMethod = Method.getMethod("com.shenjvm.KLSymbol intern(Object)");
    public static final Method longValueOfMethod = Method.getMethod("Long valueOf(long)");
    public static final Method doubleValueOfMethod = Method.getMethod("Double valueOf(double)");
    public static final Method forNameMethod =  Method.getMethod("Class forName(String)");
    public static final Method getFieldMethod = Method.getMethod("java.lang.reflect.Field getField(String)");
    public static final Method getMethod = Method.getMethod("Object get(Object)");
    public static final Method getFunMethod = Method.getMethod("com.shenjvm.KLFunction getFun(com.shenjvm.KLSymbol)");
    public static final Method setFunMethod= Method.getMethod("void setFun(com.shenjvm.KLSymbol,com.shenjvm.KLFunction)");
    public static final Method invokeMethod1 = Method.getMethod("Object invoke()");
    public static final Method invokeMethod2 = Method.getMethod("Object invoke(Object)");
    public static final Method invokeMethod3 = Method.getMethod("Object invoke(Object,Object[])");
    public static final Method getDeclaredMethodMethod = Method.getMethod("java.lang.reflect.Method getDeclaredMethod(String,Class[])");
    public static final Method setAccessibleMethod = Method.getMethod("void setAccessible(boolean)");
    public static final Method klFunctionConstructorMethod1 =
            Method.getMethod("void <init>(Class,com.shenjvm.KLSymbol[],Class[],org.objectweb.asm.Type,String,boolean,java.lang.reflect.Method)");
    public static final Method klFunctionConstructorMethod2 =
            Method.getMethod("void <init>(com.shenjvm.KLSymbol[],Class[],org.objectweb.asm.Type,boolean,java.lang.reflect.Method)");
    public static final Method klLambdaConstructorMethod1 =
            Method.getMethod("void <init>(Class,java.lang.reflect.Method,boolean)");
    public static final Method klLambdaConstructorMethod2 =
            Method.getMethod("void <init>(Class,boolean)");
    public static final Method consructorMethod1 = Method.getMethod("void <init>()");
    public static final Method newInstanceMethod = Method.getMethod("Object newInstance()");
    public static final Method runMethod = Method.getMethod("Object run()");
}