/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.step;

import com.maoyang.enforce.model.IDomainModel;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 修订后续步骤的异常.
 *
 * 配合{@link IDomainStep#execute(IDomainModel)}的异常使用，在某一步骤抛出该异常来修订后续步骤
 * 可能产生的死循环(a -> b(revise) -> a)，由使用者负责，暂时不提供dead loop检测：因为即使检测到也不知道如何处理，它本身就是bug
 * 有的最佳实践说：不要使用异常控制流程。但在这里，它更有效，不要太在意最佳实践的说法
 * IMPORTANT: 不要在领域层异常直接实现该接口，应该创建新的异常类，否则会与步骤的回滚机制冲突！推荐直接使用{@link ReviseStepsException}
 */
public interface IReviseStepsException {

    /**
     * 修订后的后续步骤编号.
     *
     * @return subsequent step code list
     */
    @NotNull
    List<String> subsequentSteps();
}
