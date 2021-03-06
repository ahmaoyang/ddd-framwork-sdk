/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.model;

/**
 * 领域模型的契约对象，持有业务行为所需要的数据.
 *
 * 它本身是JavaBean.
 * {@code Creator}模式，是为了保护领域模型：不是外部系统给模型的每个字段赋值，而是模型从{@code Creator}里挑选字段然后自行赋值.
 * 否则，领域模型会蜕变成JavaBean，无法保护自己的状态一致性、完整性、安全性等.
 */
public interface IDomainModelCreator {
}
