package com.shenjvm;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class KLStdInStream extends KLBufferedReader {
    KLStdInStream() {
        stream = new BufferedReader(new InputStreamReader(System.in));
    }
}
