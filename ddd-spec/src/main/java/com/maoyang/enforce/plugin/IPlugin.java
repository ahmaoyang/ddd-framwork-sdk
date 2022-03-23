/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.plugin;

/**
 * 插件.
 */
public interface IPlugin {

    /**
     * 获取插件码.
     *
     * 目前，对应的是{@code Pattern.code} 或 {@code Partner.code}.
     * Unique within a single JVM.
     */
    String getCode();

    /**
     * 获取当前版本.
     */
    String getVersion();
}
