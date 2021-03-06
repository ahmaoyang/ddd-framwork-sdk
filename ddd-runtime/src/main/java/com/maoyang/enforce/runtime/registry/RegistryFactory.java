/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.runtime.registry;

import com.maoyang.enforce.annotation.Domain;
import com.maoyang.enforce.annotation.DomainAbility;
import com.maoyang.enforce.annotation.DomainService;
import com.maoyang.enforce.annotation.Extension;
import com.maoyang.enforce.annotation.Partner;
import com.maoyang.enforce.annotation.Pattern;
import com.maoyang.enforce.annotation.Policy;
import com.maoyang.enforce.annotation.Specification;
import com.maoyang.enforce.annotation.Step;
import com.maoyang.enforce.ext.IPlugable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Component
@Slf4j
class RegistryFactory implements InitializingBean {
    // 有序的注册，因为他们之间有时间依赖关系
    static List<RegistryEntry> validRegistryEntries = new ArrayList<>();

    private static Map<Class<? extends Annotation>, PrepareEntry> validPrepareEntries = new HashMap<>(3);

    void register(ApplicationContext applicationContext) {
        for (RegistryEntry registryEntry : validRegistryEntries) {
            log.info("register {}'s ...", registryEntry.annotation.getSimpleName());

            for (Object springBean : applicationContext.getBeansWithAnnotation(registryEntry.annotation).values()) {
                registryEntry.create().registerBean(springBean);
            }
        }

        InternalIndexer.postIndexing();
    }

    static void preparePlugins(Class<? extends Annotation> annotation, Object bean) {
        if (!(bean instanceof IPlugable)) {
            throw BootstrapException.ofMessage(bean.getClass().getCanonicalName() + " must be IPlugable");
        }

        PrepareEntry prepareEntry = validPrepareEntries.get(annotation);
        if (prepareEntry == null) {
            throw BootstrapException.ofMessage(annotation.getCanonicalName() + " not supported");
        }

        prepareEntry.create().prepare(bean);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 注册Domain，是为了可视化，避免漏掉某些支撑域
        validRegistryEntries.add(new RegistryEntry(Domain.class, () -> new DomainDef()));
        validRegistryEntries.add(new RegistryEntry(DomainService.class, () -> new DomainServiceDef()));
        validRegistryEntries.add(new RegistryEntry(Specification.class, () -> new SpecificationDef()));
        validRegistryEntries.add(new RegistryEntry(Step.class, () -> new StepDef()));
        validRegistryEntries.add(new RegistryEntry(DomainAbility.class, () -> new DomainAbilityDef()));
        validRegistryEntries.add(new RegistryEntry(Policy.class, () -> new PolicyDef()));
        validRegistryEntries.add(new RegistryEntry(Partner.class, () -> new PartnerDef()));
        validRegistryEntries.add(new RegistryEntry(Pattern.class, () -> new PatternDef()));
        validRegistryEntries.add(new RegistryEntry(Extension.class, () -> new ExtensionDef()));

        validPrepareEntries.put(Partner.class, new PrepareEntry(() -> new PartnerDef()));
        validPrepareEntries.put(Extension.class, new PrepareEntry(() -> new ExtensionDef()));
    }

    private static class RegistryEntry {
        private final Class<? extends Annotation> annotation;
        private final Supplier<IRegistryAware> supplier;

        RegistryEntry(Class<? extends Annotation> annotation, Supplier<IRegistryAware> supplier) {
            this.annotation = annotation;
            this.supplier = supplier;
        }

        IRegistryAware create() {
            return supplier.get();
        }
    }

    private static class PrepareEntry {
        private final Supplier<IPrepareAware> supplier;

        PrepareEntry(Supplier<IPrepareAware> supplier) {
            this.supplier = supplier;
        }

        IPrepareAware create() {
            return supplier.get();
        }
    }
}
