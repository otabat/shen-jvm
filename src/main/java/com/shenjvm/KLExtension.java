package com.shenjvm;

import org.apache.commons.lang3.StringUtils;

public class KLExtension {
    public static Object jvmForwardDeclareFunction(Object fileNameObj, Object astObj) {
        KLCons ast = (KLCons)astObj;
        KLSymbol funSym = (KLSymbol)((KLCons)ast.cdr).car;

        Object args = ((KLCons)((KLCons)ast.cdr).cdr).car;
        int argSize = 0;
        while(args != KL.EMPTY_LIST) {
            ++argSize;
            args = ((KLCons)args).cdr;
        }

        KLFunction fun = (KLFunction)KLSymbol.getFun(funSym);
        if (fun != null && argSize == fun.paramTypes.length)
            return astObj;

        StringBuilder sb = new StringBuilder();
        String fqClassName = sb.append(KLCompiler.userPackageName).append(".").append(KLCompiler.evalClassNamePrefix).toString();
        String fqSlashedClassName = StringUtils.replace(fqClassName, ".", "/");
        KLCompiler.forwardDeclareFunction(funSym, argSize, fqClassName, fqSlashedClassName, null, false);
        return astObj;
    }
}