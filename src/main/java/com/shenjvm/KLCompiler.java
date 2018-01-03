package com.shenjvm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.util.TraceClassVisitor;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.ASMifier;

public class KLCompiler implements Opcodes {
    public static boolean isKLCompile = false;
    public static String defaultPackageName;
    public static String shenPackageName = "shen";
    public static String userPackageName = "user";
    public static String evalClassNamePrefix = "eval__";
    public static String lmdMethodNamePrefix = "lambda__";
    public static String closedEnvFieldNamePrefix = "l";
    public static int classFileId = 0;
    public static int methodNameId = 0;
    public static int autoIncSymNameId = 0;
    public static int lmdMethodNameId = 0;
    public static final String autoIncSymNamePrefix = "G__";
    public ClassWriter cw;
    public GeneratorAdapter runGa;
    public HashMap<KLSymbol, KLLocal> locals;
    public String packageName;
    public String fqClassName;
    public String slashedPackageName;
    public String fqInternalClassName;
    public String fqSlashedClassName;
    public Type retTypeCand;
    public Type type;
    public byte[] compiledBytes;
    public KLSymbol defunSym;
    public Label defunStartLabel;
    public boolean isStaticCompile = false;
    public static String[][] klFileNames = new String[][] {
            {"kl", "toplevel"}, {"kl-replace", "core-replace"}, {"kl", "sys"}, {"kl", "sequent"}, {"kl", "yacc"},
            {"kl", "reader"}, {"kl", "prolog"}, {"kl", "track"}, {"kl", "load"}, {"kl-overwrite", "load-overwrite"},
            {"kl", "writer"}, {"kl", "macros"}, {"kl-replace", "declarations-replace"}, {"kl-replace", "types-replace"},
            {"kl", "t-star"}, {"kl-extension", "extension"}
    };
    public static java.lang.reflect.Method defineClassMethod;
    public static String[] javaIdSearchStrings = new String[]{"-", ".", ">", "<", "'", "!", "?", "=", "/", "*", "@",
            "+", "#", "&", "~", "$"};
    public static String[] javaIdReplaceStrings = new String[]{"_", "$dt", "$gt", "$lt", "$qot", "$ex", "$qst", "$eq",
            "$sl", "$ast", "$at", "$pls", "$sh", "$amp", "$tld", "__"};

    static {
        try {
            defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass",
                    new Class[] {String.class, byte[].class, int.class, int.class});
            defineClassMethod.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public KLCompiler() {}

    public KLCompiler(boolean isStaticCompile) {
        this.isStaticCompile = isStaticCompile;
    }

    public static KLSymbol createAutoIncrementSymbol() {
        return KLPrimitive.intern(autoIncSymNamePrefix + ++autoIncSymNameId);
    }

    public void compileSymbol(GeneratorAdapter ga, KLSymbol sym) {
        KLLocal local = null;
        if (sym == KLSymbolPool.TRUE) {
            ga.getStatic(KLASM.KLSYMBOLPOOL_TYPE, "TRUE", KLASM.KLSYMBOL_TYPE);
            retTypeCand = KLASM.KLSYMBOL_TYPE;
            return;
        } else if (sym == KLSymbolPool.FALSE) {
            ga.getStatic(KLASM.KLSYMBOLPOOL_TYPE, "FALSE", KLASM.KLSYMBOL_TYPE);
            retTypeCand = KLASM.KLSYMBOL_TYPE;
            return;
        } else if (locals != null) {
            local = locals.get(sym);
            if (local == null) {
                KLASM.intern(ga, sym);
                retTypeCand = KLASM.KLSYMBOL_TYPE;
                return;
            }
            switch (local.localType) {
                case FUNCTION_PARAMETER:
                case LAMBDA_PARAMETER:
                    ga.loadArg(local.pos);
                    retTypeCand = KLASM.OBJECT_TYPE;
                    break;
                case LET_BINDING:
                    ga.loadLocal(local.pos);
                    retTypeCand = KLASM.OBJECT_TYPE;
                    break;
                case FREE_VARIABLE:
                    ga.loadArg(0);
                    ga.checkCast(local.field.decType);
                    ga.getField(local.field.decType, local.field.name, KLASM.OBJECT_TYPE);
                    retTypeCand = KLASM.OBJECT_TYPE;
                    break;
            }
            return;
        }
        KLASM.intern(ga, sym);
        retTypeCand = KLASM.KLSYMBOL_TYPE;
    }

    public void compileObject(GeneratorAdapter ga, Object o, boolean isTailCand) {
        if (o instanceof  KLSymbol)
            compileSymbol(ga, (KLSymbol)o);
        else if (o instanceof String) {
            ga.push((String)o);
            retTypeCand = KLASM.STRING_TYPE;
        } else if (o instanceof Long) {
            KLASM.boxLong(ga, o);
            retTypeCand = KLASM.LONG_TYPE;
        } else if (o instanceof Double) {
            KLASM.boxDouble(ga, o);
            retTypeCand = KLASM.DOUBLE_TYPE;
        } else if (o instanceof ArrayList)
            compileList(ga, (ArrayList)o, isTailCand);
        else
            throw new RuntimeException("Failed to compile an unknown type");
    }

    public void compileArgs(GeneratorAdapter ga, ArrayList<Object> ast) {
        for (int i = 0; i < ast.size() - 1; ++i)
            compileObject(ga, ast.get(i + 1), false);
    }

    public void compileObjects(GeneratorAdapter ga, ArrayList<Object> ast, boolean isTailCand) {
        for (int i = 0; i < ast.size(); ++i)
            compileObject(ga, ast.get(i), isTailCand);
    }

    public void arrayStoreObjects(GeneratorAdapter ga, ArrayList<Object> ast) {
        int argSize = ast.size() - 1;
        ga.push(argSize);
        ga.newArray(KLASM.OBJECT_TYPE);
        for (int i = 0; i < argSize; ++i) {
            ga.dup();
            ga.push(i);
            compileObject(ga, ast.get(i + 1), false);
            ga.arrayStore(retTypeCand);
        }
    }

    public static KLFunction forwardDeclareFunction(KLSymbol funSym, int paramSize, String fqClassName,
                                                    String fqSlashedClassName, String fqInternalClassName,
                                                    boolean isStaticCompile) {
        KLSymbol[] symParams = new KLSymbol[paramSize];
        for (int i = 0; i < paramSize; ++i)
            symParams[i] = createAutoIncrementSymbol();
        Class[] paramTypes = new Class[paramSize];
        Arrays.fill(paramTypes, Object.class);
        KLFunction fun = new KLFunction(symParams, paramTypes, KLASM.OBJECT_TYPE, isStaticCompile, null);
        KLSymbol.setFun(funSym, fun);

        if (isStaticCompile) {
            String methodName = createSafeJavaId(funSym.name);
            String methodDecl = createMethodDeclaration(methodName, paramSize);
            fun.decFQInternalClassName = fqInternalClassName;
            fun.methodDecl = methodDecl;
        }
        fun.decFQClassName = fqClassName;
        fun.decFQSlashedClassName = fqSlashedClassName;
        fun.isFwdDec = true;
        return fun;
    }

    public static ArrayList<Object> curryAndPartiallyApplyHelper(ArrayList<KLSymbol> autoSyms,
                                                                 ArrayList<Object> funAppAst, int pos) {
        ArrayList<Object> lmdAst = new ArrayList<Object>(3);
        lmdAst.add(KLSymbolPool.LAMBDA);
        lmdAst.add(autoSyms.get(pos));
        if (pos == autoSyms.size() - 1)
            lmdAst.add(funAppAst);
        else
            lmdAst.add(curryAndPartiallyApplyHelper(autoSyms, funAppAst, pos + 1));
        return lmdAst;
    }

    public void curryAndPartiallyApply(GeneratorAdapter ga, ArrayList<Object> ast, int paramSize, int argSize) {
        int autoSymNum = paramSize - argSize;
        ArrayList<KLSymbol> autoSyms = new ArrayList<KLSymbol>(autoSymNum);
        for (int i = 0; i < autoSymNum; ++i)
            autoSyms.add(createAutoIncrementSymbol());

        int funAppAstSize = ast.size() + autoSymNum;
        ArrayList<Object> funAppAst = new ArrayList<Object>(funAppAstSize);
        for (int i = 0; i < ast.size(); ++i)
            funAppAst.add(ast.get(i));
        for (int i = 0; i < autoSymNum; ++i)
            funAppAst.add(autoSyms.get(i));

        ArrayList<Object> curriedAst = curryAndPartiallyApplyHelper(autoSyms, funAppAst, 0);
        compileObject(ga, curriedAst, false);
    }

    public ArrayList<Object> partiallyApplyHelper(ArrayList<Object> funAst, ArrayList<Object> surplusArgs, int pos) {
        ArrayList<Object> lmdAppAst = new ArrayList<Object>(2);
        if (pos == 0)
            lmdAppAst.add(funAst);
        else
            lmdAppAst.add(partiallyApplyHelper(funAst, surplusArgs, pos - 1));
        lmdAppAst.add(surplusArgs.get(pos));
        return lmdAppAst;
    }

    public void partiallyApply(GeneratorAdapter ga, ArrayList<Object> ast, int paramSize, int argSize) {
        ArrayList<Object> funAppAst = new ArrayList<Object>(ast.subList(0, paramSize + 1));
        ArrayList<Object> surplusArgs = new ArrayList<Object>(ast.subList(paramSize + 1, ast.size()));
        ArrayList<Object> partialAppAst = partiallyApplyHelper(funAppAst, surplusArgs, surplusArgs.size() - 1);
        compileObject(ga, partialAppAst, false);
    }

    public void compileCurryAndPartialFunctionApplication(GeneratorAdapter ga, ArrayList<Object> ast, KLFunction fun) {
        int paramSize = fun.paramTypes.length;
        int argSize = ast.size() - 1;
        if (argSize < paramSize)
            curryAndPartiallyApply(ga, ast, paramSize, argSize);
        else
            partiallyApply(ga, ast, paramSize, argSize);
    }

    public void compileFunctionApplication(GeneratorAdapter ga, ArrayList<Object> ast, boolean isTailCand) {
        KLSymbol funSym = (KLSymbol)ast.get(0);
        KLFunction fun = (KLFunction)KLSymbol.getFun(funSym);

        if (fun == null && (locals == null || locals != null && !locals.containsKey(funSym))) {
            int paramSize = ast.size() - 1;
            fun = forwardDeclareFunction(funSym, paramSize, fqClassName, fqSlashedClassName, fqInternalClassName,
                    isStaticCompile);
        }

        int argSize = ast.size() - 1;
        if (fun!= null && argSize != fun.paramTypes.length) {
            compileCurryAndPartialFunctionApplication(ga, ast, fun);
        } else if (funSym == defunSym && isTailCand) {
            for (int i = 0; i < argSize; ++i)
                compileObject(ga, ast.get(i + 1), false);
            for (int i = argSize - 1; i >= 0; --i)
                ga.storeArg(i);
            ga.goTo(defunStartLabel);
        } else if (fun != null && fun.isStatic) {
            compileArgs(ga, ast);
            if (fun.decClass != null)
                ga.invokeStatic(Type.getType(fun.decClass), Method.getMethod(fun.methodDecl));
            else
                ga.invokeStatic(Type.getType(fun.decFQInternalClassName), Method.getMethod(fun.methodDecl));
        } else {
            KLASM.intern(ga, funSym.name);
            ga.invokeStatic(KLASM.KLSYMBOL_TYPE, KLMethodPool.getFunMethod);
            ga.checkCast(KLASM.KLFUNCTION_TYPE);
            ga.getField(KLASM.KLFUNCTION_TYPE, "method", KLASM.REFLECT_METHOD_TYPE);
            ga.push((Type)null);
            arrayStoreObjects(ga, ast);
            ga.invokeVirtual(KLASM.REFLECT_METHOD_TYPE, KLMethodPool.invokeMethod3);
        }
        retTypeCand = fun.retType;
    }

    public static void generateFunctionParameters(GeneratorAdapter ga, ArrayList<KLSymbol> params) {
        ga.push(params.size());
        ga.newArray(KLASM.KLSYMBOL_TYPE);
        for (int i = 0; i < params.size(); ++i) {
            ga.dup();
            ga.push(i);
            KLASM.intern(ga, params.get(i));
            ga.arrayStore(KLASM.KLSYMBOL_TYPE);
        }
    }

    public static void generateFunctionParameterTypes(GeneratorAdapter ga, int paramSize) {
        ga.push(paramSize);
        ga.newArray(KLASM.CLASS_TYPE);
        for (int i = 0; i < paramSize; ++i) {
            ga.dup();
            ga.push(i);
            ga.push(KLASM.OBJECT_TYPE);
            ga.arrayStore(KLASM.CLASS_TYPE);
        }
    }

    public static String createFunctionSignature(String methodName, String retTypeName, int paramSize) {
        StringBuilder funSig = new StringBuilder().append(retTypeName). append(" ").append(methodName).append("(");
        String[] classNames = new String[paramSize];
        Arrays.fill(classNames, "Object");
        String d = "";
        for (String s : classNames) {
            funSig.append(d).append(s);
            d = ",";
        }
        funSig.append(")");
        return funSig.toString();
    }

    public void generateDefunMethod(String methodName, ArrayList<KLSymbol> params, Object body, ClassWriter cw) {
        String funSig = createFunctionSignature(methodName, "Object", params.size());
        Method method = Method.getMethod(funSig);
        GeneratorAdapter ga = new GeneratorAdapter(ACC_PUBLIC + ACC_STATIC, method, null, null, cw);
        defunStartLabel = ga.mark();

        locals = new HashMap<KLSymbol, KLLocal>();
        for (int i = 0; i < params.size(); ++i) {
            KLLocal local = new KLLocal(KLLocal.LocalType.FUNCTION_PARAMETER, retTypeCand, i);
            locals.put(params.get(i), local);
        }
        compileObject(ga, body, true);
        locals = null;
        defunStartLabel = null;

        ga.returnValue();
        ga.endMethod();
    }

    public void generateGetDefunMethod(GeneratorAdapter ga, int localPos, String methodName,
                                       ArrayList<KLSymbol> params, String fqClassName) {
        ga.push(fqClassName);
        ga.invokeStatic(KLASM.CLASS_TYPE, KLMethodPool.forNameMethod);

        ga.push(methodName);
        generateFunctionParameterTypes(ga, params.size());
        ga.invokeVirtual(KLASM.CLASS_TYPE, KLMethodPool.getDeclaredMethodMethod);
        ga.storeLocal(localPos, KLASM.CLASS_TYPE);

        ga.loadLocal(localPos);
        ga.push(true);
        ga.invokeVirtual(KLASM.REFLECT_METHOD_TYPE, KLMethodPool.setAccessibleMethod);
    }

    public static String createMethodDeclaration(String methodName, int paramSize) {
        StringBuilder sb = new StringBuilder();
        sb.append("Object ");
        sb.append(methodName);
        sb.append("(");
        for (int i = 0, il = paramSize - 1; i < paramSize; ++i) {
            sb.append("Object");
            if (i < il)
                sb.append(",");
        }
        sb.append(")");
        return sb.toString();
    }

    public void generateDefunFunction(GeneratorAdapter ga, int localPos, String methodName, ArrayList<KLSymbol> params, String methodDecl) {
        ga.newInstance(KLASM.KLFUNCTION_TYPE);
        ga.dup();

        if (isStaticCompile) {
            ga.push(type);
            generateFunctionParameters(ga, params);
            generateFunctionParameterTypes(ga, params.size());
            ga.getStatic(KLASM.KLASM_TYPE, "OBJECT_TYPE", KLASM.ASM_TYPE_TYPE);

            methodDecl = createMethodDeclaration(methodName, params.size());
            ga.push(methodDecl);

            ga.push(true);
            ga.loadLocal(localPos);
            ga.invokeConstructor(KLASM.KLFUNCTION_TYPE, KLMethodPool.klFunctionConstructorMethod1);
            ga.storeLocal(localPos, KLASM.KLFUNCTION_TYPE);

        } else {
            generateFunctionParameters(ga, params);
            generateFunctionParameterTypes(ga, params.size());
            ga.getStatic(KLASM.KLASM_TYPE, "OBJECT_TYPE", KLASM.ASM_TYPE_TYPE);
            ga.push(false);
            ga.loadLocal(localPos);
            ga.invokeConstructor(KLASM.KLFUNCTION_TYPE, KLMethodPool.klFunctionConstructorMethod2);
            ga.storeLocal(localPos, KLASM.KLFUNCTION_TYPE);
        }
    }

    public static void generateRegisterDefunFunction(GeneratorAdapter ga, int localPos, String funName) {
        KLASM.intern(ga, funName);
        ga.loadLocal(localPos);
        ga.invokeStatic(KLASM.KLSYMBOL_TYPE, KLMethodPool.setFunMethod);
    }

    public static String createSafeJavaId(String s) {
        return StringUtils.replaceEach(s, javaIdSearchStrings, javaIdReplaceStrings);
    }

    public void compileDefunExpr(ArrayList ast) {
        if (ast.size() != 4)
            throw new IllegalArgumentException("Wrong number of arguments for defun");
        KLSymbol sym = (KLSymbol)ast.get(1);

        if (KLOverwrite.overwriteSyms.containsKey(sym))
            return;

        defunSym = sym;

        String funName = defunSym.name;
        KLFunction fun = (KLFunction)KLSymbol.getFun(defunSym);
        String methodName = createSafeJavaId(funName);
        if (fun != null && !fun.isFwdDec)
            methodName += "__" + ++methodNameId;

        ArrayList<KLSymbol> params = (ArrayList<KLSymbol>)ast.get(2);
        Object body = ast.get(3);

        int rungaLocalPos = 2;
        ClassWriter cw = this.cw;
        String fqClassName = this.fqClassName;
        String methodDecl = (isStaticCompile) ? createMethodDeclaration(methodName, params.size()) : null;

        generateDefunMethod(methodName, params, body, cw);
        generateGetDefunMethod(runGa, rungaLocalPos, methodName, params, fqClassName);
        generateDefunFunction(runGa, rungaLocalPos, methodName, params, methodDecl);
        generateRegisterDefunFunction(runGa, rungaLocalPos, funName);

        KLSymbol[] symParams = params.toArray(new KLSymbol[0]);
        Class[] paramTypes = new Class[params.size()];
        Arrays.fill(paramTypes, Object.class);

        if (isStaticCompile)
            fun = new KLFunction(fqInternalClassName, symParams, paramTypes, KLASM.OBJECT_TYPE, methodDecl, true);
        else
            fun = new KLFunction(symParams, paramTypes, KLASM.OBJECT_TYPE, false, null);
        KLSymbol.setFun(defunSym, fun);

        KLASM.intern(runGa, defunSym);
        retTypeCand = KLASM.KLSYMBOL_TYPE;

        defunSym = null;
    }

    public static boolean isClosedSymbolLetExpr(KLSymbol sym, ArrayList<Object> ast) {
        if (sym == ast.get(1))
            return false;
        if (sym == ast.get(2) || sym == ast.get(3))
            return true;
        if (ast.get(3) instanceof ArrayList)
            return isClosedSymbolList(sym, (ArrayList<Object>)ast.get(3));
        return false;
    }

    public static boolean isClosedSymbolLambdaExpr(KLSymbol sym, ArrayList<Object> ast) {
        if (ast.size() == 2)
            return isClosedSymbol(sym, ast.get(1));
        if (sym == ast.get(1))
            return false;
        return isClosedSymbol(sym, ast.get(2));
    }

    public static boolean isClosedSymbolList(KLSymbol sym, ArrayList<Object> ast) {
        if (ast == KL.EMPTY_LIST)
            return false;
        Object head = ast.get(0);
        if (head == KLSymbolPool.LET)
            return isClosedSymbolLetExpr(sym, ast);
        else if (head == KLSymbolPool.LAMBDA)
            return isClosedSymbolLambdaExpr(sym, ast);
        else {
            boolean isClosedSym = false;
            for (int i = 0; i < ast.size(); ++i) {
                if (isClosedSymbol(sym, ast.get(i))) {
                    isClosedSym = true;
                    break;
                }
            }
            return isClosedSym;
        }
    }

    public static boolean isClosedSymbol(KLSymbol sym, Object body) {
        if (sym == body) {
           return true;
        } else if (body instanceof ArrayList) {
            ArrayList<Object> bodyAst = (ArrayList<Object>)body;
            return isClosedSymbolList(sym, bodyAst);
        } else {
            return false;
        }
    }

    public static ArrayList<KLSymbol> findClosedSymbols(HashMap<KLSymbol, KLLocal> locals, Object body, KLSymbol lmdParam) {
        if (locals == null)
            return null;
        ArrayList<KLSymbol> closedSyms = null;
        for (KLSymbol localSym: locals.keySet()) {
            if (localSym != lmdParam && isClosedSymbol(localSym, body)) {
                if (closedSyms == null)
                    closedSyms = new ArrayList<KLSymbol>();
                closedSyms.add(localSym);
            }
        }
        return closedSyms;
    }

    public static String dottedClassNameToInternalClassName(String dottedClassName) {
        StringBuilder internalClassName = new StringBuilder();
        return internalClassName.append("L").append(StringUtils.replace(dottedClassName, ".", "/")).append(";").
                toString();
    }

    public static String slashedClassNameToInternalClassName(String slashedClassName) {
        StringBuilder internalClssName = new StringBuilder();
        return internalClssName.append("L").append(slashedClassName).append(";").toString();
    }

    public String createLambdaSignature(String methodName, int paramSize) {
        StringBuilder lmdSig = new StringBuilder().append("Object ").append(methodName).append("(com.shenjvm.KLAClosedEnv");
        if (paramSize == 1)
            lmdSig.append(",Object");
        lmdSig.append(")");
        return lmdSig.toString();
    }

    public void generateKLLambdaInstance(GeneratorAdapter ga, int paramSize) {
        ga.newInstance(KLASM.KLLAMBDA_TYPE);
        ga.dup();
        ga.push(type);
        ga.push(isStaticCompile);
        ga.invokeConstructor(KLASM.KLLAMBDA_TYPE, KLMethodPool.klLambdaConstructorMethod2);
    }

    public static void generateLambdaParameterTypes(GeneratorAdapter ga, int paramSize) {
        ga.push(paramSize + 1);
        ga.newArray(KLASM.CLASS_TYPE);

        ga.dup();
        ga.push(0);
        ga.push(KLASM.KLACLOSEDENV_TYPE);
        ga.arrayStore(KLASM.CLASS_TYPE);

        for (int i = 1; i < paramSize + 1; ++i) {
            ga.dup();
            ga.push(i);
            ga.push(KLASM.OBJECT_TYPE);
            ga.arrayStore(KLASM.CLASS_TYPE);
        }
    }

    public void compileLambdaExprHelper(GeneratorAdapter ga, ArrayList<Object> ast) {
        KLSymbol newLmdParam;
        Object body;
        int paramSize = (ast.size() == 3) ? 1 : 0;
        if (paramSize == 1) {
            newLmdParam = (KLSymbol)ast.get(1);
            body = ast.get(2);
        } else {
            newLmdParam = null;
            body = ast.get(1);
        }

        String lmdMethodName = lmdMethodNamePrefix + ++lmdMethodNameId;
        String lmdSig = createLambdaSignature(lmdMethodName, paramSize);
        Method lmdMethod = Method.getMethod(lmdSig);
        GeneratorAdapter lmdGa = new GeneratorAdapter(ACC_PUBLIC + ACC_STATIC, lmdMethod, null, null, cw);

        HashMap<KLSymbol, KLLocal> oldLocals = locals;
        HashMap<KLSymbol, KLLocal> newLocals = new HashMap<KLSymbol, KLLocal>();

        if (newLmdParam != null) {
            int lmdLocalPos = 1;
            KLLocal lmdLocal = new KLLocal(KLLocal.LocalType.LAMBDA_PARAMETER, KLASM.OBJECT_TYPE, lmdLocalPos);
            newLocals.put(newLmdParam, lmdLocal);
        }

        int lmdLocal = ga.newLocal(KLASM.KLLAMBDA_TYPE);
        generateKLLambdaInstance(ga, paramSize);
        ga.storeLocal(lmdLocal);

        ArrayList<KLSymbol> closedSyms = findClosedSymbols(oldLocals, body, newLmdParam);
        int closedSymNum = (closedSyms == null) ?  0 : closedSyms.size();
        boolean isClosedSymExist = closedSymNum > 0;
        int closedEnvLocal = 0;
        if (isClosedSymExist) {
            String fqClosedEnvInternelClassName = new StringBuilder().append("Lcom/shenjvm/KLClosedEnv").
                    append(String.valueOf(closedSymNum)).append(";").toString();

            Type closedEnvType = Type.getType(fqClosedEnvInternelClassName);
            ga.newInstance(closedEnvType);
            ga.dup();

            int fieldId = 0;
            for (KLSymbol closedSym : closedSyms) {
                KLLocal oldLocal = oldLocals.get(closedSym);
                if (oldLocal.localType == KLLocal.LocalType.FREE_VARIABLE) {
                    ga.loadArg(0);
                    ga.checkCast(oldLocal.field.decType);
                    ga.getField(oldLocal.field.decType, oldLocal.field.name, KLASM.OBJECT_TYPE);
                } else {
                    if (oldLocal.localType == KLLocal.LocalType.LET_BINDING)
                        ga.loadLocal(oldLocal.pos);
                    else
                        ga.loadArg(oldLocal.pos);
                }
                String fieldName = closedEnvFieldNamePrefix + ++fieldId;
                Type valType = (oldLocal.valType == null) ? KLASM.OBJECT_TYPE : oldLocal.valType;
                KLField field = new KLField(closedEnvType, valType, fieldName);
                KLLocal local = new KLLocal(KLLocal.LocalType.FREE_VARIABLE, valType, field);
                newLocals.put(closedSym, local);
            }

            String closedEnvSig = createFunctionSignature("<init>", "void", closedSymNum);
            ga.invokeConstructor(closedEnvType, Method.getMethod(closedEnvSig));

            closedEnvLocal = ga.newLocal(closedEnvType);
            ga.storeLocal(closedEnvLocal);
        }

        locals = newLocals;
        compileObject(lmdGa, body, false);
        locals = oldLocals;

        lmdGa.returnValue();
        lmdGa.endMethod();

        ga.loadLocal(lmdLocal);

        ga.push(type);
        ga.push(lmdMethodName);
        generateLambdaParameterTypes(ga, paramSize);
        ga.invokeVirtual(KLASM.CLASS_TYPE, KLMethodPool.getDeclaredMethodMethod);

        int methodLocal = ga.newLocal(KLASM.REFLECT_METHOD_TYPE);
        ga.storeLocal(methodLocal);

        ga.loadLocal(methodLocal);
        ga.push(true);
        ga.invokeVirtual(KLASM.REFLECT_METHOD_TYPE, KLMethodPool.setAccessibleMethod);

        ga.loadLocal(methodLocal);
        ga.putField(KLASM.KLLAMBDA_TYPE, "method", KLASM.REFLECT_METHOD_TYPE);

        if (isClosedSymExist) {
            ga.loadLocal(lmdLocal);
            ga.loadLocal(closedEnvLocal);
            ga.putField(KLASM.KLLAMBDA_TYPE, "closedEnv", KLASM.KLACLOSEDENV_TYPE);
        }

        ga.loadLocal(lmdLocal);

        retTypeCand = KLASM.KLLAMBDA_TYPE;
    }

    public Class loadClass(String className, byte[] bytes) {
        try {
            return (Class)defineClassMethod.invoke(KL.SYSTEM_CLASS_LOADER,
                   new Object[]{className, bytes, new Integer(0), new Integer(bytes.length)});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void compileLambdaExpr(GeneratorAdapter ga, ArrayList<Object> ast) {
        if (ast.size() != 3)
            throw new KLException("Wrong number of arguments for lambda");
        compileLambdaExprHelper(ga, ast);
    }

    public void compileLambdaApplication(GeneratorAdapter ga, ArrayList<Object> ast) {
        if (ast.size() == 2) {
            compileObject(ga, ast.get(1), false);
            int argLocal = ga.newLocal(KLASM.OBJECT_TYPE);
            ga.storeLocal(argLocal);

            compileObject(ga, ast.get(0), false);
            ga.checkCast(KLASM.KLLAMBDA_TYPE);
            int lmdLocal = ga.newLocal(KLASM.KLLAMBDA_TYPE);
            ga.storeLocal(lmdLocal);

            ga.loadLocal(lmdLocal);
            ga.getField(KLASM.KLLAMBDA_TYPE, "method", KLASM.REFLECT_METHOD_TYPE);
            ga.push((Type)null);

            ga.push(2);
            ga.newArray(KLASM.OBJECT_TYPE);

            ga.dup();
            ga.push(0);
            ga.loadLocal(lmdLocal);
            ga.getField(KLASM.KLLAMBDA_TYPE, "closedEnv", KLASM.KLACLOSEDENV_TYPE);
            ga.arrayStore(KLASM.OBJECT_TYPE);

            ga.dup();
            ga.push(1);
            ga.loadLocal(argLocal);
            ga.arrayStore(KLASM.OBJECT_TYPE);

            ga.invokeVirtual(KLASM.REFLECT_METHOD_TYPE, KLMethodPool.invokeMethod3);
        } else if (ast.size() == 1) {
            compileObject(ga, ast.get(0), false);
            ga.checkCast(KLASM.KLLAMBDA_TYPE);
            int lmdLocal = ga.newLocal(KLASM.KLLAMBDA_TYPE);
            ga.storeLocal(lmdLocal);

            ga.loadLocal(lmdLocal);
            ga.getField(KLASM.KLLAMBDA_TYPE, "method", KLASM.REFLECT_METHOD_TYPE);
            ga.push((Type)null);

            ga.push(1);
            ga.newArray(KLASM.OBJECT_TYPE);
            ga.dup();
            ga.push(0);
            ga.loadLocal(lmdLocal);
            ga.getField(KLASM.KLLAMBDA_TYPE, "closedEnv", KLASM.KLACLOSEDENV_TYPE);
            ga.arrayStore(KLASM.OBJECT_TYPE);

            ga.invokeVirtual(KLASM.REFLECT_METHOD_TYPE, KLMethodPool.invokeMethod3);
        } else {
            int paramSize = 1;
            int argSize = ast.size() - 1;
            partiallyApply(ga, ast, paramSize, argSize);
        }
        retTypeCand = KLASM.OBJECT_TYPE;
    }

    public void compileFunctionOrLambdaApplication(GeneratorAdapter ga, ArrayList<Object> ast, boolean isTailCand) {
        KLSymbol funSym = (KLSymbol)ast.get(0);
        if (locals != null && locals.containsKey(funSym)) {
            compileLambdaApplication(ga, ast);
            return;
        }
        compileFunctionApplication(ga, ast, isTailCand);
    }

    public void compileIfExpr(GeneratorAdapter ga, ArrayList<Object> ast, boolean isTailCand) {
        if (ast.size() != 4)
            throw new KLException("Wrong number of arguments for if: " + "actual " + (ast.size() - 1) + ", exptected 3");
        Object test = ast.get(1);
        Object then = ast.get(2);
        Object els = ast.get(3);
        if (test == KLSymbolPool.TRUE) {
            compileObject(ga, then, isTailCand);
            return;
        }
        if (test == KLSymbolPool.FALSE) {
            compileObject(ga, els, isTailCand);
            return;
        }
        compileObject(ga, test, false);
        int localPos = ga.newLocal(retTypeCand);
        ga.storeLocal(localPos);

        Label thenLabel = ga.newLabel();
        Label elseLabel = ga.newLabel();
        Label errorLabel = ga.newLabel();
        Label endLabel = ga.newLabel();

        ga.loadLocal(localPos);
        ga.getStatic(KLASM.KLSYMBOLPOOL_TYPE, "TRUE", KLASM.KLSYMBOL_TYPE);
        ga.ifCmp(KLASM.OBJECT_TYPE, ga.EQ, thenLabel);

        ga.loadLocal(localPos);
        ga.getStatic(KLASM.KLSYMBOLPOOL_TYPE, "FALSE", KLASM.KLSYMBOL_TYPE);
        ga.ifCmp(KLASM.OBJECT_TYPE, ga.EQ, elseLabel);

        ga.visitLabel(errorLabel);
        ga.throwException(KLASM.KLEXCEPTION_TYPE, "Test should be a boolean value");

        ga.visitLabel(thenLabel);
        compileObject(ga, then, isTailCand);
        ga.goTo(endLabel);

        ga.visitLabel(elseLabel);
        compileObject(ga, els, isTailCand);
        ga.goTo(endLabel);

        ga.visitLabel(endLabel);
    }

    public void compileAndExpr(GeneratorAdapter ga, ArrayList<Object> ast, boolean isTailCand) {
        if (ast.size() != 3)
            throw new KLException("Wrong number of arguments for and: " + "actual " + (ast.size() - 1) + ", exptected 2");
        ArrayList<Object> ifast = new ArrayList<Object>(4);
        ifast.add(KLSymbolPool.IF);
        ifast.add(ast.get(1));
        ifast.add(ast.get(2));
        ifast.add(KLSymbolPool.FALSE);
        compileIfExpr(ga, ifast, isTailCand);
    }

    public void compileOrExpr(GeneratorAdapter ga, ArrayList<Object> ast, boolean isTailCand) {
        if (ast.size() != 3)
            throw new KLException("Wrong number of arguments for or: " + "actual " + (ast.size() - 1) + ", exptected 2");
        ArrayList<Object> ifast = new ArrayList<Object>(4);
        ifast.add(KLSymbolPool.IF);
        ifast.add(ast.get(1));
        ifast.add(KLSymbolPool.TRUE);
        ifast.add(ast.get(2));
        compileIfExpr(ga, ifast, isTailCand);
    }

    public void compileCondExpr(GeneratorAdapter ga, ArrayList<Object> ast, boolean isTailCand) {
        if (ast.size() == 1) {
            ga.throwException(KLASM.KLEXCEPTION_TYPE, "No matching case found for cond");
            return;
        }
        ArrayList<Object> caseList = (ArrayList)ast.get(1);
        ArrayList<Object> ifast = new ArrayList<Object>(4);
        ifast.add(KLSymbolPool.IF);
        ifast.add(caseList.get(0));
        ifast.add(caseList.get(1));
        ast.remove(1);
        ifast.add(ast);
        compileIfExpr(ga, ifast, isTailCand);
    }

    public void compileLetExpr(GeneratorAdapter ga, ArrayList<Object> ast, boolean isTailCand) {
        if (ast.size() != 4)
            throw new KLException("Wrong number of arguments for let expression");
        KLSymbol bindSym = (KLSymbol)ast.get(1);

        compileObject(ga, ast.get(2), false);

        boolean isBindSymExist = false;
        if (locals == null)
            locals = new HashMap<KLSymbol, KLLocal>();
        else
            isBindSymExist = locals.containsKey(bindSym);
        int newLocalPos = ga.newLocal(KLASM.OBJECT_TYPE);
        KLLocal newLocal = new KLLocal(KLLocal.LocalType.LET_BINDING, retTypeCand, newLocalPos);
        KLLocal oldLocal = null;
        if (isBindSymExist)
            oldLocal = locals.get(bindSym);
        ga.storeLocal(newLocal.pos);
        locals.put(bindSym, newLocal);

        compileObject(ga, ast.get(3), isTailCand);
        if (isBindSymExist)
            locals.put(bindSym, oldLocal);
        else
            locals.remove(bindSym);
    }

    public void compileTrapErrorHelperExpr(GeneratorAdapter ga, ArrayList<Object> ast) {
        Label tryStartLabel = ga.newLabel();
        Label catchStartLabel = ga.newLabel();
        Label catchEndLabel = ga.newLabel();
        ga.visitTryCatchBlock(tryStartLabel, catchStartLabel, catchStartLabel, KLASM.EXCEPTION_TYPE.getInternalName());

        ga.visitLabel(tryStartLabel);
        compileObject(ga, ast.get(1), false);
        ga.goTo(catchEndLabel);

        ga.visitLabel(catchStartLabel);
        int exLocal = ga.newLocal(KLASM.EXCEPTION_TYPE);
        ga.storeLocal(exLocal);

        compileObject(ga, ast.get(2), false);
        ga.checkCast(KLASM.KLLAMBDA_TYPE);
        int lmdLocal = ga.newLocal(KLASM.KLLAMBDA_TYPE);
        ga.storeLocal(lmdLocal);

        ga.loadLocal(lmdLocal);
        ga.getField(KLASM.KLLAMBDA_TYPE, "method", KLASM.REFLECT_METHOD_TYPE);
        ga.push((Type)null);

        ga.push(2);
        ga.newArray(KLASM.OBJECT_TYPE);

        ga.dup();
        ga.push(0);
        ga.loadLocal(lmdLocal);
        ga.getField(KLASM.KLLAMBDA_TYPE, "closedEnv", KLASM.KLACLOSEDENV_TYPE);
        ga.arrayStore(KLASM.OBJECT_TYPE);

        ga.dup();
        ga.push(1);
        ga.loadLocal(exLocal);
        ga.arrayStore(KLASM.OBJECT_TYPE);

        ga.invokeVirtual(KLASM.REFLECT_METHOD_TYPE, KLMethodPool.invokeMethod3);
        retTypeCand = KLASM.OBJECT_TYPE;

        ga.visitLabel(catchEndLabel);
    }

    public void compileTrapErrorExpr(GeneratorAdapter ga, ArrayList<Object> ast) {
        if (ast.size() != 3)
            throw new IllegalArgumentException("");
        ArrayList<Object> trapErrHelperAst = new ArrayList<Object>(3);
        trapErrHelperAst.add(KLSymbolPool.TRAP_ERROR_HELPER);
        trapErrHelperAst.add(ast.get(1));
        trapErrHelperAst.add(ast.get(2));

        ArrayList<Object> freezeAst = new ArrayList<Object>(2);
        freezeAst.add(KLSymbolPool.FREEZE);
        freezeAst.add(trapErrHelperAst);

        ArrayList<Object> lmdAppAst = new ArrayList<Object>(1);
        lmdAppAst.add(freezeAst);
        compileList(ga, lmdAppAst, false);
    }

    public void compileFreezeExpr(GeneratorAdapter ga, ArrayList<Object> ast) {
        if (ast.size() != 2)
            throw new KLException("Wrong number of arguments for freeze: " + "actual " + (ast.size() - 1) + ", expected 1");
        ArrayList<Object> lmdast = new ArrayList<Object>(2);
        lmdast.add(KLSymbolPool.LAMBDA);
        lmdast.add(ast.get(1));
        compileLambdaExprHelper(ga, lmdast);
    }

    public void compileDoExpr(GeneratorAdapter ga, ArrayList<Object> ast, boolean isTailCand) {
        if (ast.size() != 3)
            throw new KLException("do expression should have two arguments");
        compileObject(ga, ast.get(1), false);
        ga.pop();
        compileObject(ga, ast.get(2), isTailCand);
    }

    public void compileList(GeneratorAdapter ga, ArrayList<Object> ast, boolean isTailCand) {
        if (ast == KL.EMPTY_LIST) {
            ga.getStatic(KLASM.KL_TYPE, "EMPTY_LIST", KLASM.ARRAY_LIST_TYPE);
            retTypeCand = KLASM.ARRAY_LIST_TYPE;
            return;
        }
        Object head = ast.get(0);
        if (head instanceof KLSymbol) {
            if (head == KLSymbolPool.IF)
                compileIfExpr(ga, ast, isTailCand);
            else if (head == KLSymbolPool.AND)
                compileAndExpr(ga, ast, isTailCand);
            else if (head == KLSymbolPool.OR)
                compileOrExpr(ga, ast, isTailCand);
            else if (head == KLSymbolPool.COND)
                compileCondExpr(ga, ast, isTailCand);
            else if (head == KLSymbolPool.DEFUN)
                compileDefunExpr(ast);
            else if (head == KLSymbolPool.LAMBDA)
                compileLambdaExpr(ga, ast);
            else if (head == KLSymbolPool.LET)
                compileLetExpr(ga, ast, isTailCand);
            else if (head == KLSymbolPool.FREEZE)
                compileFreezeExpr(ga, ast);
            else if (head == KLSymbolPool.DO)
                compileDoExpr(ga, ast, isTailCand);
            else if (head == KLSymbolPool.TRAP_ERROR)
                compileTrapErrorExpr(ga, ast);
            else if (head == KLSymbolPool.TRAP_ERROR_HELPER)
                compileTrapErrorHelperExpr(ga, ast);
            else
                compileFunctionOrLambdaApplication(ga, ast, isTailCand);
        } else if (head instanceof ArrayList)
            compileLambdaApplication(ga, ast);
    }

    public KLCompiler compileHelper(String packageName, String fqClassName, ArrayList<Object> ast) {
        this.packageName = packageName;
        slashedPackageName = packageName.replaceAll("\\.", "/");
        this.fqClassName = fqClassName;
        fqSlashedClassName = fqClassName.replaceAll("\\.", "/");
        fqInternalClassName = slashedClassNameToInternalClassName(fqSlashedClassName);
        type = Type.getType(fqInternalClassName);

        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_6, ACC_PUBLIC, fqSlashedClassName, null, "java/lang/Object", null);

        Method runMethod = KLMethodPool.runMethod;
        runGa = new GeneratorAdapter(ACC_PUBLIC + ACC_STATIC, runMethod, null, null, cw);

        compileObjects(runGa, ast, true);
        runGa.returnValue();
        runGa.endMethod();
        cw.visitEnd();

        compiledBytes = cw.toByteArray();
        //verifyASM(compiledBytes);
        //printASM(compiledBytes);
        return this;
    }

    public KLCompiler compile(String packageName, String classNameOrPrefix, ArrayList<Object> ast) {
        String safeClassNameOrPrefix = StringUtils.replace(classNameOrPrefix, "-", "_");
        if (!isStaticCompile || classNameOrPrefix == evalClassNamePrefix)
            safeClassNameOrPrefix += "__" + ++classFileId;
        String fqClassName = packageName + "." + safeClassNameOrPrefix;
        return compileHelper(packageName, fqClassName, ast);
    }

    public KLCompiler compile(String classNameOrPrefix, ArrayList<Object> ast) {
        compile(userPackageName, classNameOrPrefix, ast);
        return this;
    }

    public KLCompiler compile(ArrayList<Object> ast) {
        compileHelper(userPackageName, "eval__", ast);
        return this;
    }

    public static ArrayList<Object> readFile(String filePath) {
        try {
            File file = new File(filePath);
            FileReader fr = new FileReader(file);
            ArrayList<Object> ast = KLReader.read(fr);
            return ast;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public KLCompiler compileFile(String packageName, String classNameOrPrefix, String filePath) {
        ArrayList<Object> ast = readFile(filePath);
        compile(packageName, classNameOrPrefix, ast);
        return this;
    }

    public KLCompiler compileFile(String packageName, String classNameOrPrefix, ArrayList<Object> ast) {
        compile(packageName, classNameOrPrefix, ast);
        return this;
    }

    public static void forwardDeclareDefunFunctions(ArrayList<Object> ast, String fqClassName, boolean isStaticCompile) {
        for (int i = 0; i < ast.size(); ++i) {
            if (ast.get(i) instanceof ArrayList) {
                ArrayList<Object> subAst = (ArrayList<Object>)ast.get(i);
                if (subAst.get(0) == KLSymbolPool.DEFUN && subAst.size() == 4) {
                    KLSymbol funSym = (KLSymbol)subAst.get(1);
                    if (KLOverwrite.overwriteSyms.containsKey(funSym))
                        continue;
                    String fqSlashedClassName = StringUtils.replace(fqClassName, ".", "/");
                    String fqInternalClassName = null;
                    if (isStaticCompile)
                        fqInternalClassName = slashedClassNameToInternalClassName(fqSlashedClassName);
                    ArrayList<Object> params = (ArrayList<Object>)subAst.get(2);
                    forwardDeclareFunction(funSym, params.size(), fqClassName, fqSlashedClassName, fqInternalClassName,
                            isStaticCompile);
                }
            }
        }
    }

    public static KLCompiler[] compileFiles(String[][] fileNames, String packageName, boolean isStaticCompile) {
        ArrayList<Object>[] asts = new ArrayList[fileNames.length];
        String[] fqClassNames = new String[fileNames.length];
        for (int i = 0; i < fileNames.length; ++i) {
            String srcTypeDir = fileNames[i][0];
            String fileName = fileNames[i][1];
            String filePath = KL.shenSrcRootDir + "/" + srcTypeDir + "/" + fileName + ".kl";
            KL.d("Compiling " + srcTypeDir + "/" + fileName + ".kl");
            asts[i] = readFile(filePath);

            String safeClassName = StringUtils.replace(fileName, "-", "_");
            if (!isStaticCompile)
                safeClassName += "__" + ++classFileId;
            fqClassNames[i] = packageName + "." + safeClassName;
            forwardDeclareDefunFunctions(asts[i], fqClassNames[i], isStaticCompile);
        }
        KLCompiler[] compilers = new KLCompiler[fileNames.length];
        for (int i = 0; i < fileNames.length; ++i) {
            compilers[i] = new KLCompiler(isStaticCompile);
            compilers[i].compileHelper(packageName, fqClassNames[i], asts[i]).write();
        }
        return compilers;
    }

    public Object load() {
        Class c = loadClass(fqClassName, compiledBytes);
        Object res;
        try {
            res = c.getDeclaredMethod("run").invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public static void loadCompilersAndPrint(KLCompiler[] compilers) {
        for (int i = 0; i < compilers.length; ++i)
            KL.d(compilers[i].load());
    }

    public static void write(String classRootDir, String fqSlashedClassName, byte[] compiledBytes) {
        String classFilePath = new StringBuilder().append(classRootDir).append("/").append(fqSlashedClassName).
                append(".class").toString();
        File file = new File(classFilePath);
        file.getParentFile().mkdirs();
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(compiledBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        KL.d("Class file written to " + classFilePath);
    }

    public KLCompiler write() {
        write(KL.shenClassRootDir, fqSlashedClassName, compiledBytes);
        return this;
    }

    public static void printlnLocalObject(GeneratorAdapter ga, int local) {
        ga.loadLocal(local);
        ga.invokeStatic(Type.getType(KL.class), Method.getMethod("void d(Object)"));
    }

    public static void verifyASM(byte[] bytes) {
        PrintWriter pw = new PrintWriter(System.out, true);
        ClassReader cr = new ClassReader(bytes);
        CheckClassAdapter.verify(cr, true, pw);
    }

    public static void printASM(byte[] bytes) {
        PrintWriter pw = new PrintWriter(System.out, true);
        ASMifier asm = new ASMifier();
        TraceClassVisitor tcv = new TraceClassVisitor(null, pw);
        ClassReader cr = new ClassReader(bytes);
        cr.accept(tcv, 0);
        asm.print(pw);
    }

    public static void compileKLFiles(boolean isStaticCompile) {
        KLCompiler.compileFiles(klFileNames, "shen", isStaticCompile);
    }

    public static void compileTestKLFiles(String fileName, boolean isStaticCompile) {
        String[][] fileNames = new String[][]{{"kl", fileName}};
        KLCompiler[] compilers = KLCompiler.compileFiles(fileNames, "shen", isStaticCompile);
        loadCompilersAndPrint(compilers);
    }

    public static void compileMain() {
        ClassWriter mainCw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        String fqSlashedClassName = shenPackageName + "/main";
        mainCw.visit(V1_6, ACC_PUBLIC, fqSlashedClassName, null, "java/lang/Object", null);

        Method mainMethod = Method.getMethod("void main(String[])");
        GeneratorAdapter mainGa = new GeneratorAdapter(ACC_PUBLIC + ACC_STATIC, mainMethod, null, null, mainCw);
        mainGa.invokeStatic(KLASM.KLINIT_TYPE, Method.getMethod("void init()"));
        for (int i = 0; i < klFileNames.length; ++i) {
            String internalClassName = "L" + shenPackageName + "/" + createSafeJavaId(klFileNames[i][1]) + ";";
            mainGa.invokeStatic(Type.getType(internalClassName), Method.getMethod("Object run()"));
        }
        String toplevelInternalClassName = slashedClassNameToInternalClassName(shenPackageName + "/toplevel");
        mainGa.invokeStatic(Type.getType(toplevelInternalClassName), Method.getMethod("Object shen$dtshen()"));
        mainGa.returnValue();
        mainGa.endMethod();
        mainCw.visitEnd();

        byte[] bytes = mainCw.toByteArray();

        //verifyASM(bytes);
        //printASM(bytes);

        write(KL.shenClassRootDir, fqSlashedClassName, bytes);
    }
}