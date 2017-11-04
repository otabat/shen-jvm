package com.shenjvm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

public class KLOverwrite {
    public static HashMap<KLSymbol, KLSymbol> overwriteSyms = new HashMap<KLSymbol, KLSymbol>();

    public static KLSymbol fail() {
        return KLSymbolPool.SHEN_FAIL_BANG;
    }

    public static Long hash(Object o, Object limit) {
        return o.hashCode() % ((Long)limit).longValue();
    }

    public static KLSymbol isBoolean(Object o) {
        if (o == KLSymbolPool.TRUE || o == KLSymbolPool.FALSE)
            return KLSymbolPool.TRUE;
        return KLSymbolPool.FALSE;
    }

    public static KLSymbol isElement(Object item, Object cons) {
        while (cons instanceof KLCons) {
            if (KLPrimitive.equal(item, KLPrimitive.hd(cons)) == KLSymbolPool.TRUE)
                return KLSymbolPool.TRUE;
            cons = KLPrimitive.tl(cons);
        }
        return KLSymbolPool.FALSE;
    }

    public static KLSymbol isInteger(Object o) {
        if (o instanceof Long)
            return KLSymbolPool.TRUE;
        if (o instanceof Double) {
            Double d = (Double)o;
            if (d.doubleValue() == d.longValue())
                return KLSymbolPool.TRUE;
        }
        return KLSymbolPool.FALSE;
    }

    public static KLSymbol isSymbol(Object o) {
        if (o instanceof KLSymbol)
            return KLSymbolPool.TRUE;
        return KLSymbolPool.FALSE;
    }

    public static KLSymbol isVariable(Object o) {
        if (o instanceof KLSymbol && Character.isUpperCase(((KLSymbol)o).name.charAt(0)))
            return KLSymbolPool.TRUE;
        return KLSymbolPool.FALSE;
    }

    public static String pr(Object strObj, Object stmObj) {
        String s = (String)strObj;
        ((KLBufferedWriter)stmObj).write(s);
        return s;
    }

    public static Object readFileAsCharlist(Object filePathObj) {
        String combinedFilePath = (String)KLPrimitive.value(KLSymbolPool.EARMUFF_HOME_DIRECTORY) + (String)filePathObj;
        KLBufferedReader stm = new KLInStream(new File(combinedFilePath));
        KLCons head;
        try {
            int c = KLStream.readByte(stm);
            if (c == -1)
                return KL.EMPTY_LIST;
            head = new KLCons(Long.valueOf(c), KL.EMPTY_LIST);
            KLCons tail = head;
            while ((c = KLStream.readByte(stm)) != -1) {
                tail.cdr = new KLCons(Long.valueOf(c), KL.EMPTY_LIST);
                tail = (KLCons) tail.cdr;
            }
        } finally {
            stm.close();
        }
        return head;
    }

    public static String readFileAsString(Object filePathObj) {
        String combinedFilePath = (String)KLPrimitive.value(KLSymbolPool.EARMUFF_HOME_DIRECTORY) + (String)filePathObj;
        try {
            return FileUtils.readFileToString(new File(combinedFilePath), (String)null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object[] shenBindv(Object vecObj, Object valObj, Object idxObj) {
        return KLPrimitive.setAbsvectorElement(getPrologVectorObject(idxObj),
                KLVector.getAbsvectorElement((Object[])vecObj, 1), valObj);
    }

    public static Object[] shenCopyVectorHelper(Object[] srcVec, Object[] dstVec, int srcVecSize, int dstVecSize,
                                                Object fillObj) {
        for (int i = 1; i <= srcVecSize; ++i)
            dstVec[i] = srcVec[i];
        for (int i = srcVecSize + 1; i <= dstVecSize; ++i)
            dstVec[i] =fillObj;
        return dstVec;
    }

    public static Object[] shenCopyVector(Object srcVecObj, Object dstVecObj, Object srcVecSizeObj,
                                          Object dstVecSizeObj, Object fillObj) {
        return shenCopyVectorHelper((Object[])srcVecObj, (Object[])dstVecObj, ((Long)srcVecSizeObj).intValue(),
                ((Long)dstVecSizeObj).intValue(), fillObj);
    }

    public static Object shenDeref(Object consOrVecObj, Object idxObj) {
        while (true) {
           if (consOrVecObj instanceof KLCons) {
               return KLPrimitive.cons(shenDeref(KLPrimitive.hd(consOrVecObj), idxObj),
                       shenDeref(KLPrimitive.tl(consOrVecObj), idxObj));
           }
           if (!shenIsPvarHelper(consOrVecObj))
               return consOrVecObj;
           Object o = shenValVector(consOrVecObj, idxObj);
           if (o == KLSymbolPool.SHEN_NULL)
               return consOrVecObj;
           consOrVecObj = o;
        }
    }

    public static Object shenIncinfs() {
        KLSymbolPool.SHEN_EARMUFF_INFS.varValue =
                Long.valueOf(1L + ((Long)KLSymbolPool.SHEN_EARMUFF_INFS.varValue).longValue());
        return KLSymbolPool.SHEN_EARMUFF_INFS.varValue;
    }

    public static boolean shenIsPvarHelper(Object o) {
        if (o instanceof Object[] && ((Object[])o)[0] == KLSymbolPool.SHEN_PVAR)
            return true;
        return false;
    }

    public static KLSymbol shenIsPvar(Object o) {
        if (shenIsPvarHelper(o))
            return KLSymbolPool.TRUE;
        return KLSymbolPool.FALSE;
    }

    public static Object[] shenMkPvar(Object o) {
        Object[] vec = KLVector.createAbsvector(2);
        vec[0] = KLSymbolPool.SHEN_PVAR;
        vec[1] = o;
        return vec;
    }

    public static Object[] shenNewpv(Object idxObj) {
        long countInc =
                (Long)KLPrimitive.getAbsvectorElement(KLSymbolPool.SHEN_EARMUFF_VARCOUNTER.varValue, idxObj) + 1L;
        Long countIncObj = Long.valueOf(countInc);
        KLPrimitive.setAbsvectorElement(KLSymbolPool.SHEN_EARMUFF_VARCOUNTER.varValue, idxObj, countInc);
        Object[] vec = (Object[])getPrologVectorObject(idxObj);
        if (countInc == ((Long)vec[0]).longValue())
            shenResizeProcessVector(idxObj, countIncObj);
        return shenMkPvar(countIncObj);
    }

    public static Object getPrologVectorObject(Object idxObj) {
        return KLPrimitive.getAbsvectorElement(KLSymbolPool.SHEN_EARMUFF_PROLOGVECTORS.varValue, idxObj);
    }

    public static Object setPrologVectorObject(Object idxObj, Object valObj) {
        return KLPrimitive.setAbsvectorElement(KLSymbolPool.SHEN_EARMUFF_PROLOGVECTORS.varValue, idxObj, valObj);
    }

    public static Object[] shenResizeProcessVector(Object idxObj, Object vecSizeObj) {
        Object srcVecObj = getPrologVectorObject(idxObj);
        int vecSize = ((Long)vecSizeObj).intValue();
        int dstVecSize = vecSize + vecSize;
        Object[] dstVec = shenResizeVector(srcVecObj, Long.valueOf(dstVecSize), KLSymbolPool.SHEN_NULL);
        return (Object[])setPrologVectorObject(idxObj, dstVec);
    }

    public static Object[] shenResizeVector(Object srcVecObj, Object dstVecSizeObj, Object fillObj) {
        Object[] srcVec = (Object[])srcVecObj;
        int srcVecSize = ((Long)srcVec[0]).intValue();
        Object[] dstVec = KLVector.createAbsvector(((Long)dstVecSizeObj).intValue() + 1);
        int dstVecSize = ((Long)dstVecSizeObj).intValue();
        dstVec[0] = dstVecSizeObj;
        return shenCopyVectorHelper(srcVec, dstVec, srcVecSize, dstVecSize, fillObj);
    }

    public static Object[] shenUnbindv(Object vecObj, Object idxObj) {
        return KLPrimitive.setAbsvectorElement(getPrologVectorObject(idxObj),
                KLVector.getAbsvectorElement((Object[])vecObj, 1), KLSymbolPool.SHEN_NULL);
    }

    public static Object shenValVector(Object vecObj, Object idxObj) {
        return KLPrimitive.getAbsvectorElement(getPrologVectorObject(idxObj),
        KLVector.getAbsvectorElement((Object[])vecObj, 1));
    }

    public static Object shenLazyderef(Object pvarObj, Object idxObj) {
        while (shenIsPvarHelper(pvarObj)) {
            Object o = shenValVector(pvarObj, idxObj);
            if (o == KLSymbolPool.SHEN_NULL)
                break;
            pvarObj = o;
        }
        return pvarObj;
    }
}