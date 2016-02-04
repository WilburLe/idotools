package com.toolbox.framework.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
* @author E-mail:86yc@sina.com
* 
*/
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface SetMongoCollection {

    public String collection() default "";
}
