package com.shenjvm;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

public class KLStdOutStream extends KLBufferedWriter {
    KLStdOutStream() {
        stream = new BufferedWriter(new OutputStreamWriter(System.out));
    }
}
