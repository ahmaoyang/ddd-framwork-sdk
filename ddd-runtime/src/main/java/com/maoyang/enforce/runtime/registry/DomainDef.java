/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.runtime.registry;

import com.maoyang.enforce.annotation.Domain;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
class DomainDef implements IRegistryAware {

    @Getter
    private String code;

    @Getter
    private String name;

    @Getter
    private Object domainBean;

    @Override
    public void registerBean(@NotNull Object bean) {
        Domain domain = InternalAopUtils.getAnnotation(bean, Domain.class);
        this.code = domain.code();
        this.name = domain.name();
        this.domainBean = bean;

        InternalIndexer.index(this);
    }
}
