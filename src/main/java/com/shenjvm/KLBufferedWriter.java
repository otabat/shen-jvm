package com.shenjvm;

import java.io.BufferedWriter;
import java.io.IOException;

public abstract class KLBufferedWriter implements KLIStream {
    public KLSymbol direction = KLSymbolPool.OUT;
    public BufferedWriter stream;

    public void write(String s) {
        try {
            stream.write(s);
            stream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeByte(int x) {
        try {
            stream.write(x);
            stream.flush();
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