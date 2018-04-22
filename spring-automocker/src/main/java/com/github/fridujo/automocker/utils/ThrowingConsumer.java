package com.github.fridujo.automocker.utils;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T> {

    static <T> Consumer<T> silent(ThrowingConsumer<T> throwing) {
        return t -> {
            try {
                throwing.accept(t);
            } catch (Exception e) {
                throw LamdaLuggageException.wrap(e);
            }
        };
    }

    void accept(T t) throws Exception;
}
