/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.runtime.registry;

import javax.validation.constraints.NotNull;

interface IPrepareAware {

    void prepare(@NotNull Object bean);
}
