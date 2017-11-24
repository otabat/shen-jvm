package com.shenjvm;

import java.lang.reflect.Method;

public class KLLambda {
    public Class decClass;
    public Method method;
    public boolean isStatic;
    public KLAClosedEnv closedEnv;

    public KLLambda(Class decClass, boolean isStatic) {
        this.decClass = decClass;
        this.isStatic = isStatic;
    }
}