/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.runtime.registry;

import com.maoyang.enforce.annotation.Specification;
import com.maoyang.enforce.specification.ISpecification;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
class SpecificationDef implements IRegistryAware {

    @Getter
    private String name;

    @Getter
    private String[] tags;

    @Getter
    private ISpecification specificationBean;

    @Override
    public void registerBean(@NotNull Object bean) {
        Specification specification = InternalAopUtils.getAnnotation(bean, Specification.class);
        this.name = specification.value();
        this.tags = specification.tags();
        if (!(bean instanceof ISpecification)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implement ISpecification");
        }
        this.specificationBean = (ISpecification) bean;

        InternalIndexer.index(this);
    }
}
