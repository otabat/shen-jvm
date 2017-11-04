package com.shenjvm;

import java.util.ArrayList;

public class KLCons {
    public Object car;
    public Object cdr;

    KLCons(Object car, Object cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public ArrayList<Object> toArrayList(boolean isNonProperListAllowed) {
        ArrayList<Object> list = new ArrayList<Object>();
        KLCons c = this;
        while (true) {
            if (c.car instanceof KLCons)
                list.add(((KLCons)c.car).toArrayList(isNonProperListAllowed));
            else
                list.add(c.car);
            if (!(c.cdr instanceof KLCons))
                break;
            c = (KLCons)c.cdr;
        }
        if (c.cdr != KL.EMPTY_LIST) {
            if (!isNonProperListAllowed)
                throw new RuntimeException("Attempted to convert a non-proper list of cons to a list");
            list.add(c.cdr);
        }
        return list;
    }

    public ArrayList<Object> toArrayList() {
        return toArrayList(false);
    }

    @Override
    public String toString() {
        return this.toArrayList(true).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof KLCons) {
            KLCons cons = (KLCons)o;
            return (KLPrimitive.equal(this.car, cons.car) == KLSymbolPool.TRUE) &&
                    (KLPrimitive.equal(this.cdr, cons.cdr) == KLSymbolPool.TRUE);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (this.car == null ? 0 : this.car.hashCode());
        hashCode = 31 * hashCode + (this.cdr == null ? 0 : this.cdr.hashCode());
        return hashCode;
    }
}