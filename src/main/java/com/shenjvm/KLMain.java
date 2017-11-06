package com.shenjvm;

public class KLMain {
    public static void main(String[] args) {
        boolean isStaticCompile = true;
        KLCompiler.defaultPackageName = KLCompiler.shenPackageName;
        KLCompiler.isKLCompile = true;
        KLInit.init();
        KLCompiler.compileKLFiles(isStaticCompile);
        KLCompiler.compileMain();
        //KLCompiler.compileTestKLFiles("test39", isStaticCompile);
    }
}