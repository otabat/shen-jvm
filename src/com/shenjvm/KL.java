package com.shenjvm;

import java.util.ArrayList;

public class KL {
    public static final ArrayList<Object> EMPTY_LIST = new ArrayList<Object>();
    public static final ClassLoader SYSTEM_CLASS_LOADER = ClassLoader.getSystemClassLoader();
    public static final String rootDir = System.getProperty("user.dir");
    public static final String shenRootDir = rootDir + "/shen";
    public static final String shenClassRootDir = rootDir + "/out/production/shen-jvm";
    public static final String shenSrcRootDir = shenRootDir + "/src";
    public static final KLIStream STD_INPUT_STREAM = new KLStdInStream();
    public static final KLIStream STD_OUTPUT_STREAM = new KLStdOutStream();

    public static void d(Object o) {
        System.out.println(o);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T)o;
    }
}