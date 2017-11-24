package com.shenjvm;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class KLPrimitive {
    public static KLSymbol intern(Object name) {
        KLSymbol sym = KLSymbol.symNameToSymMap.get(name);
        if (sym == null) {
            String strName = (String)name;
            sym = new KLSymbol(strName);
            KLSymbol.symNameToSymMap.put(strName, sym);
        }
        return sym;
    }

    public static String pos(Object s, Object idx) {
        return KLString.get((String)s, ((Long)idx).intValue());
    }

    public static String tlstr(Object s) {
        return ((String)s).substring(1);
    }

    public static String cn(Object s1, Object s2) {
        return (String)s1 + (String)s2;
    }

    public static String str(Object x) {
        return x.toString();
    }

    public static KLSymbol isString(Object o) {
        if (o instanceof String)
            return KLSymbolPool.TRUE;
        return KLSymbolPool.FALSE;
    }

    public static String nToString(Object codePoint) {
        return KLString.nToString(((Long)codePoint).intValue());
    }

    public static Long stringToN(Object s) {
        return Long.valueOf(KLString.stringToN((String)s));
    }

    public static Object value(Object o) {
        KLSymbol sym = (KLSymbol)o;
        if (sym.varValue == null)
            throw(new KLException("Undefined variable " + sym));
        return sym.varValue;
    }

    public static Object set(Object sym, Object val) {
        ((KLSymbol)sym).varValue = val;
        return val;
    }

    public static Void simpleError(Object errorMsg) {
        throw new KLException((String)errorMsg);
    }

    public static String errorToString(Object e) {
        if (!(e instanceof Exception))
            throw new KLException(e + " is not an exception");
        Throwable t = KLException.getRootCause((Throwable)e);
        if (t instanceof KLException)
            return t.getMessage();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    public static KLCons cons(Object car, Object cdr) {
       return new KLCons(car, cdr);
    }

    public static Object hd(Object cons) {
        return ((KLCons)cons).car;
    }

    public static Object tl(Object cons) {
        return ((KLCons)cons).cdr;
    }

    public static KLSymbol isCons(Object o) {
        if (o instanceof KLCons)
            return KLSymbolPool.TRUE;
        return KLSymbolPool.FALSE;
    }

    public static KLSymbol equal(Object o1, Object o2) {
        if (o1.equals(o2))
            return KLSymbolPool.TRUE;
        if (o1 instanceof Number) {
            if (o2 instanceof Number) {
                if (((Number)o1).doubleValue() == ((Number)o2).doubleValue())
                    return KLSymbolPool.TRUE;
                return KLSymbolPool.FALSE;
            }
        }
        if (o1 instanceof Object[]) {
            if (o2 instanceof Object[]) {
                Object[] oa1 = (Object[])o1;
                Object[] oa2 = (Object[])o2;
                if (oa1.length != oa2.length)
                    return KLSymbolPool.FALSE;
                for (int i = 0; i < oa1.length; ++i)
                    if (equal(oa1[i], oa2[i]) == KLSymbolPool.FALSE)
                        return KLSymbolPool.FALSE;
                return KLSymbolPool.TRUE;
            }
        }
        return KLSymbolPool.FALSE;
    }

    public static Object evalKL(Object o) {
        if (o instanceof KLCons) {
            ArrayList<Object> list = ((KLCons)o).toArrayList(false);
            ArrayList<Object> ast = new ArrayList<Object>(1);
            ast.add(list);
            String packageName = (KLCompiler.defaultPackageName == null) ?
                    KLCompiler.userPackageName : KLCompiler.defaultPackageName;
            return new KLCompiler().compile(packageName, KLCompiler.evalClassNamePrefix, ast).load();
        }
        return o;
    }

    public static Object type(Object o, Object type) {
        return o;
    }

    public static Object[] absvector(Object n) {
        return KLVector.createAbsvector(((Long)n).intValue());
    }

    public static Object[] setAbsvectorElement(Object ary, Object idx, Object val) {
        return KLVector.setAbsvectorElement((Object[])ary, ((Long)idx).intValue(), val);
    }

    public static Object getAbsvectorElement(Object ary, Object idx) {
        return KLVector.getAbsvectorElement((Object[])ary, ((Long)idx).intValue());
    }

    public static KLSymbol isAbsvector(Object o) {
        if (o instanceof Object[])
            return KLSymbolPool.TRUE;
        return KLSymbolPool.FALSE;
    }

    public static Long writeByte(Object x, Object stm) {
        Long l = (Long)x;
        KLStream.writeByte(l.intValue(), (KLBufferedWriter)stm);
        return l;
    }

    public static Long readByte(Object stm) {
        return Long.valueOf(KLStream.readByte((KLBufferedReader)stm));
    }

    public static KLIStream open(Object filePath, Object direction) {
        File filePathFile = new File((String)filePath);
        String combinedFilePath;
        if (filePathFile.isAbsolute())
            combinedFilePath = (String)KLPrimitive.value(KLSymbolPool.EARMUFF_HOME_DIRECTORY) + filePath;
        else {
            StringBuilder sb = new StringBuilder();
            combinedFilePath = sb.append(KL.rootDir).append("/").
                    append((String)KLPrimitive.value(KLSymbolPool.EARMUFF_HOME_DIRECTORY)).append(filePath).toString();
        }
        File file = new File(combinedFilePath);
        KLIStream stm;
        if (direction == KLSymbolPool.IN)
            stm = new KLInStream(file);
        else if (direction == KLSymbolPool.OUT)
            stm = new KLOutStream(file);
        else
            throw new KLException("Unknown stream direction");
        return stm;
    }

    public static ArrayList<Object> close(Object stm) {
        ((KLIStream)stm).close();
        return KL.EMPTY_LIST;
    }

    public static Number getTime(Object type) {
        return KLSystem.getTime((KLSymbol)type);
    }

    public static Number add(Object x, Object y) {
       if (x instanceof Long) {
           if (y instanceof Long)
               return (Long)x + (Long)y;
           return (Long)x + (Double)y;
       }
       if (y instanceof Long)
           return (Double)x + (Long)y;
       return (Double)x + (Double)y;
    }

    public static Number subtract(Object x, Object y) {
       if (x instanceof Long) {
           if (y instanceof Long)
               return (Long)x - (Long)y;
           return (Long)x - (Double)y;
       }
       if (y instanceof Long)
           return (Double)x - (Long)y;
       return (Double)x - (Double)y;
    }

    public static Number multiply(Object x, Object y) {
       if (x instanceof Long) {
           if (y instanceof Long)
               return (Long)x * (Long)y;
           return (Long)x * (Double)y;
       }
       if (y instanceof Long)
           return (Double)x * (Long)y;
       return (Double)x * (Double)y;
    }

    public static Number divide(Object x, Object y) {
       if (x instanceof Long) {
           if (y instanceof Long) {
               if ((Long)x % (Long)y == 0)
                   return (Long)x / (Long)y;
               else
                   return (Long)x / ((Long) y).doubleValue();
           }
           Double yD = (Double)y;
           if (yD == 0.0)
               throw new ArithmeticException("/ by zero");
           return (Long)x / yD;
       }
       if (y instanceof Long)
           return (Double)x / (Long)y;
       Double yD = (Double)y;
       if (yD == 0.0)
           throw new ArithmeticException("/ by zero");
       return (Double)x / yD;
    }

    public static KLSymbol greater(Object x, Object y) {
       if (x instanceof Long) {
           if (y instanceof Long) {
               if ((Long)x > (Long)y)
                   return KLSymbolPool.TRUE;
               return KLSymbolPool.FALSE;
           }
           if ((Long)x > (Double)y)
               return KLSymbolPool.TRUE;
           return KLSymbolPool.FALSE;
       }
       if (y instanceof Long) {
           if ((Double)x > (Long)y)
               return KLSymbolPool.TRUE;
           return KLSymbolPool.FALSE;
       }
       if ((Double)x > (Double)y)
           return KLSymbolPool.TRUE;
       return KLSymbolPool.FALSE;
    }

    public static KLSymbol less(Object x, Object y) {
       if (x instanceof Long) {
           if (y instanceof Long) {
               if ((Long)x < (Long)y)
                   return KLSymbolPool.TRUE;
               return KLSymbolPool.FALSE;
           }
           if ((Long)x < (Double)y)
               return KLSymbolPool.TRUE;
           return KLSymbolPool.FALSE;
       }
       if (y instanceof Long) {
           if ((Double)x < (Long)y)
               return KLSymbolPool.TRUE;
           return KLSymbolPool.FALSE;
       }
       if ((Double)x < (Double)y)
           return KLSymbolPool.TRUE;
       return KLSymbolPool.FALSE;
    }

    public static KLSymbol greaterOrEqual(Object x, Object y) {
       if (x instanceof Long) {
           if (y instanceof Long) {
               if ((Long)x >= (Long)y)
                   return KLSymbolPool.TRUE;
               return KLSymbolPool.FALSE;
           }
           if ((Long)x >= (Double)y)
               return KLSymbolPool.TRUE;
           return KLSymbolPool.FALSE;
       }
       if (y instanceof Long) {
           if ((Double)x >= (Long)y)
               return KLSymbolPool.TRUE;
           return KLSymbolPool.FALSE;
       }
       if ((Double)x >= (Double)y)
           return KLSymbolPool.TRUE;
       return KLSymbolPool.FALSE;
    }

    public static KLSymbol lessOrEqual(Object x, Object y) {
       if (x instanceof Long) {
           if (y instanceof Long) {
               if ((Long)x <= (Long)y)
                   return KLSymbolPool.TRUE;
               return KLSymbolPool.FALSE;
           }
           if ((Long)x <= (Double)y)
               return KLSymbolPool.TRUE;
           return KLSymbolPool.FALSE;
       }
       if (y instanceof Long) {
           if ((Double)x <= (Long)y)
               return KLSymbolPool.TRUE;
           return KLSymbolPool.FALSE;
       }
       if ((Double)x <= (Double)y)
           return KLSymbolPool.TRUE;
       return KLSymbolPool.FALSE;
    }

    public static KLSymbol isNumber(Object o) {
        if (o instanceof Number)
            return KLSymbolPool.TRUE;
        return KLSymbolPool.FALSE;
    }
}