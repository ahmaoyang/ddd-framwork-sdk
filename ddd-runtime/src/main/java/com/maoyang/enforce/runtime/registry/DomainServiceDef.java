/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.runtime.registry;

import com.maoyang.enforce.annotation.DomainService;
import com.maoyang.enforce.model.IDomainService;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
class DomainServiceDef implements IRegistryAware {

    @Getter
    private String domain;

    @Getter
    private IDomainService domainServiceBean;

    @Override
    public void registerBean(@NotNull Object bean) {
        DomainService domainService = InternalAopUtils.getAnnotation(bean, DomainService.class);
        if (!(bean instanceof IDomainService)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implement IDomainService");
        }

        this.domain = domainService.domain();
        this.domainServiceBean = (IDomainService) bean;

        InternalIndexer.index(this);
    }
}
