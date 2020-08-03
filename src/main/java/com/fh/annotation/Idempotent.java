package com.fh.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)//注解的声明周期
public @interface Idempotent {
}
