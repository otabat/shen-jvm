package com.shenjvm;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.objectweb.asm.Type;

public class KLFunction {
    public Class decClass;
    public KLSymbol[] params;
    public Class[] paramTypes;
    public Type retType;
    public String methodDecl;
    public boolean isStatic;
    public Method method;
    public String decFQClassName;
    public String decFQSlashedClassName;
    public String decFQInternalClassName;
    public boolean isFwdDec = false;

    public KLFunction(Class decClass, Class[] paramTypes, Type retType, String methodDecl, boolean isStatic) {
        this.decClass = decClass;
        this.paramTypes = paramTypes;
        this.retType = retType;
        this.methodDecl = methodDecl;
        this.isStatic = isStatic;
    }

    public KLFunction(Class decClass, Class[] paramTypes, Type retType, String methodDecl, boolean isStatic,
                      Method method) {
        this.decClass = decClass;
        this.paramTypes = paramTypes;
        this.retType = retType;
        this.methodDecl = methodDecl;
        this.isStatic = isStatic;
        this.method = method;
    }

    public KLFunction(Class decClass, KLSymbol[] params, Class[] paramTypes, Type retType, String methodDecl,
                      boolean isStatic, Method method) {
        this.decClass = decClass;
        this.params = params;
        this.paramTypes = paramTypes;
        this.retType = retType;
        this.methodDecl = methodDecl;
        this.isStatic = isStatic;
        this.method = method;
    }

    public KLFunction(KLSymbol[] params, Class[] paramTypes, Type retType, boolean isStatic, Method method) {
        this.params = params;
        this.paramTypes = paramTypes;
        this.retType = retType;
        this.isStatic = isStatic;
        this.method = method;
    }

    public KLFunction(String decFQInternalClassName, KLSymbol[] params, Class[] paramTypes, Type retType, String methodDecl, boolean isStatic) {
        this.decFQInternalClassName = decFQInternalClassName;
        this.params = params;
        this.paramTypes = paramTypes;
        this.retType = retType;
        this.methodDecl = methodDecl;
        this.isStatic = isStatic;
    }

    @Override
    public String toString() {
        return  "#<KLFunction decClass: " + decClass +
                ", params: " + ((params == null) ? params : Arrays.asList(this.params)) +
                ", paramTypes: " + ((paramTypes == null) ? paramTypes : Arrays.asList(paramTypes)) +
                ", retType: " + this.retType.getClassName() +
                ", isStatic: " + this.isStatic +
                ", method: " + method +
                ">";
    }
}