package com.shenjvm;

public class KLInit {
    public static void initPrimitives() throws ClassNotFoundException, NoSuchMethodException {
        KLFunction internFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.KLSYMBOL_TYPE,
                "com.shenjvm.KLSymbol intern(Object)", true);
        KLFunction posFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.STRING_TYPE, "String pos(Object,Object)", true);
        KLFunction tlstrFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.STRING_TYPE,
                "String tlstr(Object)", true);
        KLFunction cnFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class}, KLASM.STRING_TYPE,
                "String cn(Object,Object)", true);
        KLFunction strFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.STRING_TYPE,
                "String str(Object)", true);
        KLFunction isStringFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.KLSYMBOL_TYPE,
                "com.shenjvm.KLSymbol isString(Object)", true);
        KLFunction nToStringFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.STRING_TYPE,
                "String nToString(Object)", true);
        KLFunction stringToNFun =  new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.LONG_TYPE,
                "Long stringToN(Object)", true);
        KLFunction setFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.OBJECT_TYPE, "Object set(Object,Object)", true);
        KLFunction valueFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.OBJECT_TYPE,
                "Object value(Object)", true);
        KLFunction simpleErrorFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.VOID_TYPE,
                "Void simpleError(Object)", true);
        KLFunction errorToStringFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class},
                KLASM.STRING_TYPE, "String errorToString(Object)", true);
        KLFunction consFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.KLCONS_TYPE, "com.shenjvm.KLCons cons(Object,Object)", true);
        KLFunction hdFun =  new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.OBJECT_TYPE,
                "Object hd(Object)", true);
        KLFunction tlFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.OBJECT_TYPE,
                "Object tl(Object)", true);
        KLFunction isConsFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.KLSYMBOL_TYPE,
                "com.shenjvm.KLSymbol isCons(Object)", true);
        KLFunction equalFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.KLSYMBOL_TYPE, "com.shenjvm.KLSymbol equal(Object,Object)", true);
        KLFunction evalKLFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.OBJECT_TYPE,
                "Object evalKL(Object)", true);
        KLFunction typeFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.OBJECT_TYPE, "Object type(Object,Object)", true);
        KLFunction absvectorFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.OBJECT_ARRAY_TYPE,
                "Object[] absvector(Object)", true);
        KLFunction setAbsvectorElementFun = new KLFunction(KLPrimitive.class,
                new Class[]{Object.class, Object.class, Object.class}, KLASM.OBJECT_ARRAY_TYPE,
                "Object[] setAbsvectorElement(Object,Object,Object)", true);
        KLFunction getAbsvectorElementFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.OBJECT_TYPE, "Object getAbsvectorElement(Object,Object)", true);
        KLFunction isAbsvectorFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.KLSYMBOL_TYPE,
                "com.shenjvm.KLSymbol isAbsvector(Object)", true);
        KLFunction writeByteFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.LONG_TYPE, "Long writeByte(Object,Object)", true);
        KLFunction readByteFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.LONG_TYPE,
                "Long readByte(Object)", true);
        KLFunction openFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.KLISTREAM_TYPE, "com.shenjvm.KLIStream open(Object,Object)", true);
        KLFunction closeFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.OBJECT_TYPE,
                "java.util.ArrayList close(Object)", true);
        KLFunction getTimeFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.NUMBER_TYPE,
                "Number getTime(Object)", true);
        KLFunction addFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.NUMBER_TYPE, "Number add(Object,Object)", true);
        KLFunction subtractFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.NUMBER_TYPE, "Number subtract(Object,Object)", true);
        KLFunction multiplyFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.NUMBER_TYPE, "Number multiply(Object,Object)", true);
        KLFunction divideFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.NUMBER_TYPE, "Number divide(Object,Object)", true);
        KLFunction greaterFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.KLSYMBOL_TYPE, "com.shenjvm.KLSymbol greater(Object,Object)", true);
        KLFunction lessFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.KLSYMBOL_TYPE, "com.shenjvm.KLSymbol less(Object,Object)", true);
        KLFunction greaterOrEqualFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.KLSYMBOL_TYPE, "com.shenjvm.KLSymbol greaterOrEqual(Object,Object)", true);
        KLFunction lessOrEqualFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class, Object.class},
                KLASM.KLSYMBOL_TYPE, "com.shenjvm.KLSymbol lessOrEqual(Object,Object)", true);
        KLFunction isNumberFun = new KLFunction(KLPrimitive.class, new Class[]{Object.class}, KLASM.KLSYMBOL_TYPE,
                "com.shenjvm.KLSymbol isNumber(Object)", true);

        KLSymbol.setFun(KLSymbolPool.INTERN, internFun);
        KLSymbol.setFun(KLSymbolPool.POS, posFun);
        KLSymbol.setFun(KLSymbolPool.TLSTR, tlstrFun);
        KLSymbol.setFun(KLSymbolPool.CN, cnFun);
        KLSymbol.setFun(KLSymbolPool.STR, strFun);
        KLSymbol.setFun(KLSymbolPool.IS_STRING, isStringFun);
        KLSymbol.setFun(KLSymbolPool.N_TO_STRING, nToStringFun);
        KLSymbol.setFun(KLSymbolPool.STRING_TO_N, stringToNFun);
        KLSymbol.setFun(KLSymbolPool.SET, setFun);
        KLSymbol.setFun(KLSymbolPool.VALUE, valueFun);
        KLSymbol.setFun(KLSymbolPool.SIMPLE_ERROR, simpleErrorFun);
        KLSymbol.setFun(KLSymbolPool.ERROR_TO_STRING, errorToStringFun);
        KLSymbol.setFun(KLSymbolPool.CONS, consFun);
        KLSymbol.setFun(KLSymbolPool.HD, hdFun);
        KLSymbol.setFun(KLSymbolPool.TL, tlFun);
        KLSymbol.setFun(KLSymbolPool.IS_CONS, isConsFun);
        KLSymbol.setFun(KLSymbolPool.EQUAL, equalFun);
        KLSymbol.setFun(KLSymbolPool.EVAL_KL, evalKLFun);
        KLSymbol.setFun(KLSymbolPool.TYPE, typeFun);
        KLSymbol.setFun(KLSymbolPool.ABSVECTOR, absvectorFun);
        KLSymbol.setFun(KLSymbolPool.SET_ABSVECTOR_ELEMENT, setAbsvectorElementFun);
        KLSymbol.setFun(KLSymbolPool.GET_ABSVECTOR_ELEMENT, getAbsvectorElementFun);
        KLSymbol.setFun(KLSymbolPool.IS_ABSVECTOR, isAbsvectorFun);
        KLSymbol.setFun(KLSymbolPool.WRITE_BYTE, writeByteFun);
        KLSymbol.setFun(KLSymbolPool.READ_BYTE, readByteFun);
        KLSymbol.setFun(KLSymbolPool.OPEN, openFun);
        KLSymbol.setFun(KLSymbolPool.CLOSE, closeFun);
        KLSymbol.setFun(KLSymbolPool.GET_TIME, getTimeFun);
        KLSymbol.setFun(KLSymbolPool.ADD, addFun);
        KLSymbol.setFun(KLSymbolPool.SUBTRACT, subtractFun);
        KLSymbol.setFun(KLSymbolPool.MULTIPLY, multiplyFun);
        KLSymbol.setFun(KLSymbolPool.DIVIDE, divideFun);
        KLSymbol.setFun(KLSymbolPool.GREATER, greaterFun);
        KLSymbol.setFun(KLSymbolPool.LESS, lessFun);
        KLSymbol.setFun(KLSymbolPool.GREATER_OR_EQUAL, greaterOrEqualFun);
        KLSymbol.setFun(KLSymbolPool.LESS_OR_EQUAL, lessOrEqualFun);
        KLSymbol.setFun(KLSymbolPool.IS_NUMBER, isNumberFun);
    }

    public static void initOverwrites() throws ClassNotFoundException, NoSuchMethodException {
        KLFunction failFun = new KLFunction(KLOverwrite.class, new Class[]{}, KLASM.KLSYMBOL_TYPE,
                "com.shenjvm.KLSymbol fail()", true);
        KLFunction hashFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class, Object.class}, KLASM.LONG_TYPE,
                "Long hash(Object,Object)", true);
        KLFunction isBooleanFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class}, KLASM.KLSYMBOL_TYPE,
                "com.shenjvm.KLSymbol isBoolean(Object)", true);
        KLFunction isElementFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class, Object.class},
                KLASM.KLSYMBOL_TYPE, "com.shenjvm.KLSymbol isElement(Object,Object)", true);
        KLFunction isIntegerFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class}, KLASM.KLSYMBOL_TYPE,
                "com.shenjvm.KLSymbol isInteger(Object)", true);
        KLFunction isSymbolFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class}, KLASM.KLSYMBOL_TYPE,
                "com.shenjvm.KLSymbol isSymbol(Object)", true);
        KLFunction isVariableFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class}, KLASM.KLSYMBOL_TYPE,
                "com.shenjvm.KLSymbol isVariable(Object)", true);
        KLFunction prFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class, Object.class}, KLASM.STRING_TYPE,
                "String pr(Object,Object)", true);
        KLFunction readFileAsCharlistFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class},
                KLASM.OBJECT_TYPE, "Object readFileAsCharlist(Object)", true);
        KLFunction readFileAsStringFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class}, KLASM.STRING_TYPE,
                "String readFileAsString(Object)", true);
        KLFunction shenBindvFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class, Object.class,
                Object.class}, KLASM.OBJECT_ARRAY_TYPE, "Object[] shenBindv(Object,Object,Object)", true);
        KLFunction shenCopyVectorFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class, Object.class,
                Object.class, Object.class, Object.class}, KLASM.OBJECT_ARRAY_TYPE,
                "Object[] shenCopyVector(Object,Object,Object,Object,Object)", true);
        KLFunction shenDerefFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class, Object.class},
                KLASM.OBJECT_TYPE, "Object shenDeref(Object,Object)", true);
        KLFunction shenIncinfsFun = new KLFunction(KLOverwrite.class, new Class[]{}, KLASM.OBJECT_TYPE,
                "Object shenIncinfs()", true);
        KLFunction shenIsPvarFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class}, KLASM.KLSYMBOL_TYPE,
                "com.shenjvm.KLSymbol shenIsPvar(Object)", true);
        KLFunction shenLazyderefFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class, Object.class},
                KLASM.OBJECT_TYPE, "Object shenLazyderef(Object,Object)", true);
        KLFunction shenMkPvarFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class}, KLASM.OBJECT_ARRAY_TYPE,
                "Object[] shenMkPvar(Object)", true);
        KLFunction shenNewpvFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class}, KLASM.OBJECT_ARRAY_TYPE,
                "Object[] shenNewpv(Object)", true);
        KLFunction shenResizeProcessVectorFun = new KLFunction(KLOverwrite.class,
                new Class[]{Object.class, Object.class}, KLASM.OBJECT_ARRAY_TYPE,
                "Object[] shenResizeProcessVector(Object,Object)", true);
        KLFunction shenResizeVectorFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class, Object.class,
                Object.class}, KLASM.OBJECT_ARRAY_TYPE, "Object[] shenResizeVector(Object,Object,Object)", true);
        KLFunction shenUnbindvFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class, Object.class},
                KLASM.OBJECT_ARRAY_TYPE, "Object[] shenUnbindv(Object,Object)", true);
        KLFunction shenValVectorFun = new KLFunction(KLOverwrite.class, new Class[]{Object.class, Object.class},
                KLASM.OBJECT_TYPE, "Object shenValVector(Object,Object)", true);

        KLSymbol.setFun(KLSymbolPool.FAIL, failFun);
        KLSymbol.setFun(KLSymbolPool.HASH, hashFun);
        KLSymbol.setFun(KLSymbolPool.IS_BOOLEAN, isBooleanFun);
        KLSymbol.setFun(KLSymbolPool.IS_ELEMENT, isElementFun);
        KLSymbol.setFun(KLSymbolPool.IS_INTEGER, isIntegerFun);
        KLSymbol.setFun(KLSymbolPool.IS_SYMBOL, isSymbolFun);
        KLSymbol.setFun(KLSymbolPool.IS_VARIABLE, isVariableFun);
        KLSymbol.setFun(KLSymbolPool.PR, prFun);
        KLSymbol.setFun(KLSymbolPool.READ_FILE_AS_CHARLIST, readFileAsCharlistFun);
        KLSymbol.setFun(KLSymbolPool.READ_FILE_AS_STRING, readFileAsStringFun);
        KLSymbol.setFun(KLSymbolPool.SHEN_BINDV, shenBindvFun);
        KLSymbol.setFun(KLSymbolPool.SHEN_COPY_VECTOR, shenCopyVectorFun);
        KLSymbol.setFun(KLSymbolPool.SHEN_DEREF, shenDerefFun);
        KLSymbol.setFun(KLSymbolPool.SHEN_INCINFS, shenIncinfsFun);
        KLSymbol.setFun(KLSymbolPool.SHEN_IS_PVAR, shenIsPvarFun);
        KLSymbol.setFun(KLSymbolPool.SHEN_LAZYDEREF, shenLazyderefFun);
        KLSymbol.setFun(KLSymbolPool.SHEN_MK_PVAR, shenMkPvarFun);
        KLSymbol.setFun(KLSymbolPool.SHEN_NEWPV, shenNewpvFun);
        KLSymbol.setFun(KLSymbolPool.SHEN_RESIZE_PROCESS_VECTOR, shenResizeProcessVectorFun);
        KLSymbol.setFun(KLSymbolPool.SHEN_RESIZE_VECTOR, shenResizeVectorFun);
        KLSymbol.setFun(KLSymbolPool.SHEN_UNBINDV, shenUnbindvFun);
        KLSymbol.setFun(KLSymbolPool.SHEN_VALVECTOR, shenValVectorFun);

        KLOverwrite.overwriteSyms.put(KLSymbolPool.FAIL, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.HASH, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.IS_BOOLEAN, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.IS_ELEMENT, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.IS_INTEGER, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.IS_SYMBOL, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.IS_VARIABLE, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.PR, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.READ_FILE_AS_CHARLIST, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.READ_FILE_AS_STRING, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.SHEN_BINDV, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.SHEN_COPY_VECTOR, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.SHEN_DEREF, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.SHEN_INCINFS, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.SHEN_IS_PVAR, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.SHEN_LAZYDEREF, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.SHEN_MK_PVAR, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.SHEN_NEWPV, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.SHEN_RESIZE_PROCESS_VECTOR, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.SHEN_RESIZE_VECTOR, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.SHEN_UNBINDV, KLSymbolPool.TRUE);
        KLOverwrite.overwriteSyms.put(KLSymbolPool.SHEN_VALVECTOR, KLSymbolPool.TRUE);
    }

    public static void initExtensions() {
        KLFunction quitFun = new KLFunction(KLExtension.class, new Class[]{}, KLASM.OBJECT_TYPE,"Object quit()", true);
        KLFunction sjForwardDeclareFunctionFun = new KLFunction(KLExtension.class, new Class[]{Object.class,
                Object.class}, KLASM.OBJECT_TYPE, "Object sjForwardDeclareFunction(Object,Object)", true);
        KLFunction sjPrintlnFun = new KLFunction(KLExtension.class, new Class[]{Object.class}, KLASM.OBJECT_TYPE,
                "Object sjPrintln(Object)", true);

        KLSymbol.setFun(KLSymbolPool.QUIT, quitFun);
        KLSymbol.setFun(KLSymbolPool.SHEN_JVM_FORWARD_DECLARE_FUNCTION, sjForwardDeclareFunctionFun);
        KLSymbol.setFun(KLSymbolPool.SHEN_JVM_PRINTLN, sjPrintlnFun);
    }

    public static void initGlobalVariables() {
        KLPrimitive.set(KLSymbolPool.EARMUFF_ARGV, KLPrimitive.cons("shen", KL.EMPTY_LIST));
        KLPrimitive.set(KLSymbolPool.EARMUFF_HOME_DIRECTORY, "");
        KLPrimitive.set(KLSymbolPool.EARMUFF_IMPLEMENTATION, "Java");
        KLPrimitive.set(KLSymbolPool.EARMUFF_LANGUAGE, "Java");
        KLPrimitive.set(KLSymbolPool.EARMUFF_PORT, "0.2.4-SNAPSHOT");
        KLPrimitive.set(KLSymbolPool.EARMUFF_PORTERS, "Tatsuya Tsuda");
        KLPrimitive.set(KLSymbolPool.EARMUFF_STERROR, KL.STD_OUTPUT_STREAM);
        KLPrimitive.set(KLSymbolPool.EARMUFF_STOUTPUT, KL.STD_OUTPUT_STREAM);
        KLPrimitive.set(KLSymbolPool.EARMUFF_STINPUT, KL.STD_INPUT_STREAM);
    }

    public static void init() {
        try {
            initGlobalVariables();
            initOverwrites();
            initPrimitives();
            initExtensions();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
