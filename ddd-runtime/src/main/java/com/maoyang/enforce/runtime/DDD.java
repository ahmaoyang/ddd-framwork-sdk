
package com.maoyang.enforce.runtime;

import com.maoyang.enforce.ext.IDomainExtension;
import com.maoyang.enforce.model.IDomainModel;
import com.maoyang.enforce.runtime.registry.InternalIndexer;
import com.maoyang.enforce.runtime.registry.StepDef;
import com.maoyang.enforce.step.IDomainStep;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DDD框架的核心类，定位核心对象.
 */
public final class DDD {
    private DDD() {
    }

    /**
     * 定位一个领域能力点实例.
     *
     * @param domainAbilityClazz 领域能力类型
     * @param <T>                领域能力类型
     * @return null if bug found：研发忘记使用注解DomainAbility了
     */
    @NotNull
    public static <T extends BaseDomainAbility> T findAbility(@NotNull Class<? extends T> domainAbilityClazz) {
        return InternalIndexer.findDomainAbility(domainAbilityClazz);
    }

    /**
     * 根据步骤编号定位领域活动步骤.
     *
     * @param activityCode 所属领域活动编号
     * @param stepCodeList 领域步骤编号列表
     * @param <Step>       领域步骤类型
     * @return 如果没找到，会返回数量为0的非空列表
     */
    @NotNull
    public static <Step extends IDomainStep> List<Step> findSteps(@NotNull String activityCode, @NotNull List<String> stepCodeList) {
        List<StepDef> stepDefs = InternalIndexer.findDomainSteps(activityCode, stepCodeList);
        List<Step> result = new ArrayList<>(stepDefs.size());
        for (StepDef stepDef : stepDefs) {
            result.add((Step) stepDef.getStepBean());
        }

        return result;
    }

    /**
     * 绕过 {@link BaseDomainAbility}，直接获取扩展点实例.
     *
     * 有的控制点：
     *
     * 不需要默认的扩展点实现
     * 不会有复杂的 {@link IReducer} 逻辑，取到第一个匹配的即可
     * 没有很强的业务属性：它可能是出于技术考虑而抽象出来的，而不是业务抽象
     *
     * 这些场景下，{@link BaseDomainAbility} 显得有些多此一举，可直接使用 {@link DDDAbility#firstExtension(Class, IDomainModel)}
     *
     * @param extClazz 扩展点类型
     * @param model    领域模型，用于定位扩展点
     * @param <Ext>
     * @param <R>
     */
    @NotNull
    public static <Ext extends IDomainExtension, R> Ext firstExtension(@NotNull Class<Ext> extClazz, IDomainModel model) {
        ExtensionInvocationHandler<Ext, R> proxy = new ExtensionInvocationHandler(extClazz, model, null, null, 0);
        return proxy.createProxy();
    }

    /**
     * 定位某一个领域步骤实例.
     *
     * @param activityCode 所属领域活动编号
     * @param stepCode     步骤编号
     * @param <Step>       领域步骤类型
     * @return 如果找不到，则返回null
     */
    public static <Step extends IDomainStep> Step getStep(@NotNull String activityCode, @NotNull String stepCode) {
        List<Step> steps = findSteps(activityCode, Arrays.asList(stepCode));
        if (steps.size() == 1) {
            return steps.get(0);
        }

        return null;
    }

}
