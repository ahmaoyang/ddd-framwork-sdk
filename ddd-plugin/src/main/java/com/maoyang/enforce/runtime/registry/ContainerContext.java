/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.runtime.registry;

import com.maoyang.enforce.plugin.IContainerContext;
import org.springframework.context.ApplicationContext;

import javax.validation.constraints.NotNull;

/**
 * 默认的容器上下文实现.
 */
final class ContainerContext implements IContainerContext {
    private final ApplicationContext containerApplicationContext;

    ContainerContext(ApplicationContext containerApplicationContext) {
        this.containerApplicationContext = containerApplicationContext;
    }

    @Override
    public <T> T getBean(@NotNull Class<T> requiredType) throws RuntimeException {
        return containerApplicationContext.getBean(requiredType);
    }

    @Override
    public <T> T getBean(@NotNull String name, @NotNull Class<T> requiredType) throws RuntimeException {
        return containerApplicationContext.getBean(name, requiredType);
    }
}
