package com.github.fridujo.automocker.utils;

@SuppressWarnings("serial")
public class LamdaLuggageException extends RuntimeException {

    LamdaLuggageException(Exception cause) {
        super(cause);
    }

    public static RuntimeException wrap(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new LamdaLuggageException(e);
        }
    }
}
