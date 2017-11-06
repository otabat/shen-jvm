package com.shenjvm;

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
}