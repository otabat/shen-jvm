package com.shenjvm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class KLOutStream extends KLBufferedWriter {
    KLOutStream (File file) {
        try {
            stream = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            throw(new RuntimeException(e));
        }
    }
}