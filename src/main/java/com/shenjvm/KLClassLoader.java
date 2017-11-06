package com.shenjvm;

public class KLClassLoader extends ClassLoader {
    public KLClassLoader() {
        super();
    }

    public Class defineClass(String name, byte[] bytes) {
        return defineClass(name, bytes, 0, bytes.length);
    }
}