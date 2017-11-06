package com.shenjvm;

import org.objectweb.asm.Type;

public class KLLocal {
    public LocalType localType;
    public Type valType;
    public int pos;
    public KLField field;

    public enum LocalType {
        FUNCTION_PARAMETER,
        LAMBDA_PARAMETER,
        LET_BINDING,
        FREE_VARIABLE,
        EXTERNAL_FREE_VARIABLE
    }

    public KLLocal(LocalType localType, Type valType, int pos) {
        this.localType = localType;
        this.valType = valType;
        this.pos = pos;
    }

    public KLLocal(LocalType localType, Type valType, KLField field) {
        this.localType = localType;
        this.valType = valType;
        this.field = field;
    }
}