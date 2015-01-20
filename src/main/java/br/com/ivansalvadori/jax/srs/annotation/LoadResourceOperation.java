package br.com.ivansalvadori.jax.srs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadResourceOperation {

    Class<?> expectedClass() default Object.class;

    String expectedId() default "";

    String returnedId() default "";

    Class<?> returnedClass() default Object.class;

    Class<?> options() default Object.class;

    String[] id() default "";

}
