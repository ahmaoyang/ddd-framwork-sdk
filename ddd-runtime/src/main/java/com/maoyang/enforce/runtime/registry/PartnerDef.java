/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.runtime.registry;

import com.maoyang.enforce.ext.IDomainExtension;
import com.maoyang.enforce.ext.IIdentityResolver;
import com.maoyang.enforce.model.IDomainModel;
import com.maoyang.enforce.annotation.Partner;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@ToString
class PartnerDef implements IRegistryAware, IPrepareAware, IIdentityResolver {

    @Getter
    private String code;

    @Getter
    private String name;

    @Getter
    private IIdentityResolver partnerBean;

    @Getter(AccessLevel.PACKAGE)
    private Map<Class<? extends IDomainExtension>, ExtensionDef> extensionDefMap = new HashMap<>();

    @Override
    public void registerBean(@NotNull Object bean) {
        initialize(bean);

        InternalIndexer.index(this);
    }

    @Override
    public void prepare(@NotNull Object bean) {
        initialize(bean);

        InternalIndexer.prepare(this);
    }

    private void initialize(Object bean) {
        Partner partner = InternalAopUtils.getAnnotation(bean, Partner.class);
        this.code = partner.code();
        this.name = partner.name();

        if (!(bean instanceof IIdentityResolver)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName(), " MUST implements IIdentityResolver");
        }
        this.partnerBean = (IIdentityResolver) bean;
    }

    void registerExtensionDef(ExtensionDef extensionDef) {
        Class<? extends IDomainExtension> extClazz = extensionDef.getExtClazz();
        if (extensionDefMap.containsKey(extClazz)) {
            throw BootstrapException.ofMessage("Partner(code=", code, ") can hold ONLY one instance on ", extClazz.getCanonicalName(),
                    ", existing ", extensionDefMap.get(extClazz).toString(), ", illegal ", extensionDef.toString());
        }

        extensionDefMap.put(extClazz, extensionDef);
    }

    ExtensionDef getExtension(Class<? extends IDomainExtension> extClazz) {
        return extensionDefMap.get(extClazz);
    }

    @Override
    public boolean match(@NotNull IDomainModel model) {
        return partnerBean.match(model);
    }

}
