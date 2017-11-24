package com.shenjvm;

import org.objectweb.asm.Type;

public class KLField {
    public Type decType;
    public Type type;
    public String name;

    public KLField(Type decType, Type type, String name) {
        this.decType = decType;
        this.type = type;
        this.name = name;
    }
}