/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.annotation;

import com.maoyang.enforce.step.IDomainStep;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 领域活动步骤，注解在{@link IDomainStep}之上.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Service
public @interface Step {
    @AliasFor(annotation = Service.class, attribute = "value") String value() default "";

    /**
     * 该步骤的名称.
     */
    String name() default "";

    /**
     * 该步骤所属分组.
     * <p>
     * <p>有的步骤非常大，例如：订单商品校验，涉及非常多的逻辑</p>
     * <p>这时候可以把它拆成多个步骤，但统一到“商品校验”分组里</p>
     * <p>分组，可以理解为标签：tag</p>
     */
    String[] tags() default {};

    /**
     * 该步骤依赖哪些其他步骤.
     * <p>
     * <p>即，被依赖的步骤先执行，才能执行本步骤</p>
     */
    Class<? extends IDomainStep>[] dependsOn() default {};
}
