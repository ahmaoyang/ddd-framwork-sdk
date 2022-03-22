/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.runtime.registry;

import javax.validation.constraints.NotNull;

interface IRegistryAware {

    void registerBean(@NotNull Object bean);
}
