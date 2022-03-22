/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.annotation;

import com.maoyang.enforce.ext.IIdentityResolver;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 业务前台身份，需要实现{@link IIdentityResolver}接口.
 * <p>
 * <p>垂直业务是不会叠加的，而是互斥的，他们比较的维度是单一的、固定的</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface Partner {

    /**
     * 前台垂直业务编号.
     */
    String code();

    /**
     * 前台垂直业务名称.
     */
    String name();
}
