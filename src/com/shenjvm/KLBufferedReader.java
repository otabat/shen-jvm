package com.shenjvm;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class KLBufferedReader implements KLIStream {
    public KLSymbol direction = KLSymbolPool.IN;
    public BufferedReader stream;

    public int readByte() {
        try {
            return stream.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}