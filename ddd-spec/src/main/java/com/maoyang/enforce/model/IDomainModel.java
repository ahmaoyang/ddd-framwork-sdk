/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.model;

/**
 * 领域模型，对应DDD的聚合根.
 *
 * 世界由客体组成，主体认识客体的过程也是主体改造客体的过程
 * {@code IDomainModel}是客体，{@code IDomainService}是主体
 * 客体是拟物化，体现状态；主体是拟人化，体现过程
 * 应用程序的本质是认识世界（读），和改造世界（写）的过程
 * 不要强行充血，把主体改变客体的逻辑写到领域模型里：认清主体和客体的关系！
 *
 * 领域对象为限界上下文(BC)中受保护对象，绝对不应该将其暴露到外面！
 */
public interface IDomainModel {
}
