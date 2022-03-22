/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.annotation;

import com.maoyang.enforce.model.IDomainService;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 领域服务，注解在{@link IDomainService}之上.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Service
public @interface DomainService {

    /**
     * 所属业务域.
     *
     * @return {@link Domain#code()}
     */
    String domain();
}
