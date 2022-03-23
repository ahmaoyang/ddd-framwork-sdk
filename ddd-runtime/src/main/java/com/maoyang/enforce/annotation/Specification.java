/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.annotation;

import com.maoyang.enforce.specification.ISpecification;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 业务约束规则，注解在{@link ISpecification}之上.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface Specification {

    /**
     * 业务约束名称.
     */
    String value();

    /**
     * 该业务约束规则所属标签.
     * 通过标签，对业务约束规则进行归类
     */
    String[] tags() default {};

}
