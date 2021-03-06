/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一的RPC API系统入参，注意与业务入参分开.
 */
@ToString
@Getter
@Setter
public class RequestProfile implements Serializable {
    private static final long serialVersionUID = 9532184905327019L;

    /**
     * consumer传递的请求跟踪id，用于跟踪调用链，provider负责链式传递，不会根据它做业务逻辑.
     */
    private String traceId;


    /**
     * 与{@code java.util.Locale}里的概念一致：{language}-{country}.
     *
     * 其中，zh是语言(language)，CN是国家(country)
     */
    private String locale;

    /**
     * 时区.
     *
     * e,g. GMT+8
     */
    private String timezone;

    /**
     * 预留的扩展属性.
     *
     * 由consumer/provider自行约定扩展属性的内容
     */
    private Map<String, String> ext = new HashMap<>();

}
