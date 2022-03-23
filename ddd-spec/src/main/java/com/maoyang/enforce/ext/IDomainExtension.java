/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.ext;

/**
 * 扩展点：业务语义确定，但执行逻辑不同的业务功能点，即以不变应万变.
 *
 * 通过扩展点这个接口，实现业务的多态.
 * 扩展点是分层的，在此基础上实现了：领域步骤的多态，领域模型的多态
 * ATTENTION: 扩展点方法的返回值，必须是Java类，而不能是int/boolean等primitive types，否则可能抛出NPE!
 * Extensions provide a mechanism for extending the underlying logic of a service.
 * This makes it so that extending teams can implement extension logic in an interface-driven way without modifying the core code of the underlying platform.
 */
public interface IDomainExtension extends IPlugable {
    String DefaultCode = "_default__";
}
