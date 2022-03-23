/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.plugin;

/**
 * 插件的启动、关闭监听器，是插件包加载卸载的入口.
 */
public interface IPluginListener {

    /**
     * 插件包prepare完成时触发.
     *
     * 此时，插件包里的类刚刚加载和实例化，还没有被调用
     *
     * @param ctx 容器上下文
     * @throws Exception
     */
    void onPrepared(IContainerContext ctx) throws Exception;

    /**
     * 插件包切换完成时触发.
     *
     * 此时，相应的请求会发送该插件包里的类
     *
     * @param ctx 容器上下文
     * @throws Exception
     */
    void onCommitted(IContainerContext ctx) throws Exception;
}
