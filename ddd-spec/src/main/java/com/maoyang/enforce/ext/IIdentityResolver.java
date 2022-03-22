/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.ext;

import com.maoyang.enforce.model.IDomainModel;

import javax.validation.constraints.NotNull;

/**
 * 业务身份解析器.
 */
public interface IIdentityResolver<Model extends IDomainModel> extends IPlugable {

    /**
     * 根据领域模型判断是否属于我的业务.
     *
     * @param model 领域模型
     * @return true if yes
     */
    boolean match(@NotNull Model model);
}
