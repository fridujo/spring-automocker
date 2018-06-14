package com.github.fridujo.automocker.utils;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationsTest {

    @Test
    void search_annotated_annotations() {
        Set<Annotations.AnnotatedAnnotation<MarkingAnnotation>> annotationSet = Annotations.getAnnotationsAnnotatedWith(SampleAnnotatedClass.class, MarkingAnnotation.class);

        assertThat(annotationSet)
            .as("Annotated annotations")
            .hasSize(2)
            .are(new Condition<>(
                aa -> isAnnotationOfType(aa.parentAnnotation(), MarkingAnnotation.class),
                "@MarkingAnnotation is always the parent")
            )
            .<Class<? extends Annotation>>extracting(aa -> aa.annotation().annotationType())
            .containsOnly(AnnotatedAnnotation1.class, AnnotatedAnnotation2.class);
    }

    private boolean isAnnotationOfType(Annotation a, Class<? extends Annotation> type) {
        return type.isAssignableFrom(a.annotationType());
    }

    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface MarkingAnnotation {

    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)

    @MarkingAnnotation
    private @interface AnnotatedAnnotation1 {

    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)

    @MarkingAnnotation
    @AnnotatedAnnotation1
    private @interface AnnotatedAnnotation2 {

    }

    @AnnotatedAnnotation1
    @AnnotatedAnnotation2
    private static class SampleAnnotatedClass {

    }
}
