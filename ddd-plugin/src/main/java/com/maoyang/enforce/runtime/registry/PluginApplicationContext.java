/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.runtime.registry;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 每个Plugin特有的Spring application context，使用Plugin特有的class loader
 */
class PluginApplicationContext extends ClassPathXmlApplicationContext {
    private final ClassLoader pluginClassLoader;

    PluginApplicationContext(String[] configLocations, ApplicationContext parent, ClassLoader pluginClassLoader) {
        super(configLocations, false, parent);
        this.pluginClassLoader = pluginClassLoader;
    }

    @Override
    protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader) {
        super.initBeanDefinitionReader(reader);
        reader.setBeanClassLoader(pluginClassLoader);
        setClassLoader(pluginClassLoader);
    }
}
