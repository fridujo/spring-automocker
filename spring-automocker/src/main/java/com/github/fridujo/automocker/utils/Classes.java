package com.github.fridujo.automocker.utils;

import java.lang.reflect.Field;

public final class Classes {

    private Classes() {
    }

    public static <T> T instanciate(Class<? extends T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Cannot instanciate class " + clazz.getName() + ": " + e.getMessage(), e);
        }
    }

    public static boolean isPresent(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return false;
        }
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot load class " + className);
        }
    }

    public static <T> T getValueFromProtectedField(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return (T) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Cannot find field " + fieldName + " in class " + clazz.getName(), e);
        }
    }
}
