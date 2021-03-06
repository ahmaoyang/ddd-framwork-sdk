/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一的RPC API返回结果包装类.
 *
 * @param <T> 携带的返回结果类型
 */
@Getter
@Setter
public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = 9532184905327019L;

    private String code;

    private String message;

    private T data;

    /**
     * 预留的扩展属性，面对业务的需要可自行拓展
     *
     * 由consumer/provider自行约定扩展属性的内容
     */
    private Map<String, String> ext = new HashMap<>();

}
