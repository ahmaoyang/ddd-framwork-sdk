/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.specification;

import com.maoyang.enforce.model.IDomainModel;

import javax.validation.constraints.NotNull;

/**
 * Specification declaration，是一种显式的业务规则，一种业务约束条件.
 *
 * 通过{@link ISpecification}，可以把业务规则显性化，而不是散落在各处，便于复用.
 * 同时，由于{@link ISpecification}的统一定义，也可以进行编排，统一处理.
 * {@link ISpecification}，is part of UL(Ubiquitous Language).
 *
 * @param <T> The candidate business object.
 */
public interface ISpecification<T extends IDomainModel> {

    /**
     * Check whether a candidate business object satisfies the specification: the business rule.
     *
     * @param candidate The candidate business object
     * @return true if the business rule satisfied
     */
    default boolean satisfiedBy(@NotNull T candidate) {
        return satisfiedBy(candidate, null);
    }

    /**
     * Check whether a candidate business object satisfies the specification: the business rule.
     *
     * @param candidate    The candidate business object
     * @param notification Collect reasons why specification not satisfied. If null, will not collect unsatisfaction reasons.
     * @return true if the business rule satisfied
     */
    boolean satisfiedBy(@NotNull T candidate, Notification notification);
}
