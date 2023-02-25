package ru.unlegit.resthooks.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RequestHeaders.class)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface RequestHeader {

    String name();

    String value();
}