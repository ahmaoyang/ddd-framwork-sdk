/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.step;

import com.maoyang.enforce.model.IDomainModel;

import javax.validation.constraints.NotNull;

/**
 * 可以回滚的活动步骤.
 *
 * Sagas模式
 */
public interface IRevokableDomainStep<Model extends IDomainModel, Ex extends RuntimeException> extends IDomainStep<Model, Ex> {

    /**
     * 执行本步骤的回滚操作，进行冲正.
     *
     * Best effort就好，Sagas模式并不能严格保证一致性
     *
     * @param model 领域模型
     * @param cause {@link IDomainStep#execute(IDomainModel)}执行过程中抛出的异常，即回滚原因
     */
    void rollback(@NotNull Model model, @NotNull Ex cause);
}
