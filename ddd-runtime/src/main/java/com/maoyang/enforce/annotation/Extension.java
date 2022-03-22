/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.annotation;

import com.maoyang.enforce.ext.IDomainExtension;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 扩展点，注解在{@link IDomainExtension}之上.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface Extension {

    @AliasFor(annotation = Component.class, attribute = "value") String value() default "";

    /**
     * 扩展点编号
     */
    String code();

    /**
     * 扩展点名称.
     */
    String name() default "";
}
