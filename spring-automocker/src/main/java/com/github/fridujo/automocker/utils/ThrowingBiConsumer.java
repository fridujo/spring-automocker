package com.github.fridujo.automocker.utils;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ThrowingBiConsumer<T, U> {

    static <T, U> BiConsumer<T, U> silent(ThrowingBiConsumer<T, U> throwing) {
        return (t, u) -> {
            try {
                throwing.accept(t, u);
            } catch (Exception e) {
                throw LamdaLuggageException.wrap(e);
            }
        };
    }

    void accept(T t, U u) throws Exception;
}
