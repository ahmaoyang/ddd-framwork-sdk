/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.annotation;

import com.maoyang.enforce.runtime.BaseDomainAbility;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 领域能力，注解在{@link BaseDomainAbility}之上.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface DomainAbility {


    @AliasFor(annotation = Component.class, attribute = "value") String value() default "";

    /**
     * 所属业务域.
     *
     * @return {@link Domain#code()}
     */
    String domain();

    /**
     * 能力名称.
     */
    String name() default "";

    /**
     * 该领域能力的业务标签.
     *
     * 通过标签，把众多的扩展点管理起来，结构化
     */
    String[] tags() default {};
}
