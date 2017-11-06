package com.shenjvm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class KLInStream extends KLBufferedReader {
    public File file;
    KLInStream(File file) {
        this.file = file;
        try {
            stream = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw(new RuntimeException(e));
        }
    }
}