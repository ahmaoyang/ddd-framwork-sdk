/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.model;

/**
 * 领域服务.
 *
 * 领域服务是主体，主体认识和改造客体({@code IDomainModel})
 * 本框架内，领域服务根据粒度的粗细分为3层：
 * <pre>
 *               +--------------------+
 *               |  BaseDomainAbility |
 *      +-----------------------------|
 *      |                 IDomainStep |
 * +----------------------------------|
 * |                   IDomainService |
 * +----------------------------------+
 * </pre>
 */
public interface IDomainService {
}
