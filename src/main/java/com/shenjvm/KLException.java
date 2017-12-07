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

    public static Throwable getRootCause(Throwable throwable) {
        Throwable slowPointer = throwable;
        boolean advanceSlowPointer = false;

        Throwable cause;
        while ((cause = throwable.getCause()) != null) {
            throwable = cause;
            if (throwable == slowPointer)
                throw new IllegalArgumentException("Loop detected", throwable);
            if (advanceSlowPointer)
                slowPointer = slowPointer.getCause();
            advanceSlowPointer = !advanceSlowPointer;
        }
        return throwable;
    }

    public static String getStringStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
}