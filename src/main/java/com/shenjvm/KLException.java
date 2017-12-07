package com.shenjvm;

import java.io.PrintWriter;
import java.io.StringWriter;

public class KLException extends RuntimeException{
    public KLException() {
        super();
    }

    public KLException(String msg) {
        super(msg);
    }

    public KLException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public KLException(Throwable cause) {
        super(cause);
    }

    public static Throwable getRootCause(Throwable t) {
        Throwable slowPtr = t;
        boolean advSlowPtr = false;

        Throwable cause;
        while ((cause = t.getCause()) != null) {
            t = cause;
            if (t == slowPtr)
                throw new IllegalArgumentException("Loop detected", t);
            if (advSlowPtr)
                slowPtr = slowPtr.getCause();
            advSlowPtr = !advSlowPtr;
        }
        return t;
    }

    public static String getStringStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
}