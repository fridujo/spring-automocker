package com.github.fridujo.automocker.utils;

import java.util.function.Function;

@FunctionalInterface
public interface ThrowingFunction<T, R> {

    static <T, R> Function<T, R> silent(ThrowingFunction<T, R> throwing) {
        return silent(throwing, e -> {
            throw LamdaLuggageException.wrap(e);
        });
    }

    static <T, R> Function<T, R> silent(ThrowingFunction<T, R> throwing,
                                        Function<Exception, R> errorHandler) {
        return t -> {
            try {
                return throwing.apply(t);
            } catch (Exception e) {
                return errorHandler.apply(e);
            }
        };
    }

    R apply(T t) throws Exception;
}
