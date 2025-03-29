package io.github.cassiboy1203.staffManagerLib.exceptions;

public class ClassNotAnBeanException extends RuntimeException {
    public ClassNotAnBeanException() {
        super();
    }

    public ClassNotAnBeanException(String message) {
        super(message);
    }

    public ClassNotAnBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassNotAnBeanException(Throwable cause) {
        super(cause);
    }

    protected ClassNotAnBeanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
