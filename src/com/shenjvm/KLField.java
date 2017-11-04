package com.shenjvm;

import org.objectweb.asm.Type;

public class KLField {
    public Type decType;
    public String decClassName;
    public String decClassFieldName;
    public Type type;
    public String name;
    public boolean isStatic;

    public KLField(Type decType, String decClassName, Type type, String name, boolean isStatic) {
        this.decType = decType;
        this.decClassName = decClassName;
        this.type = type;
        this.name = name;
        this.isStatic = isStatic;
    }
}