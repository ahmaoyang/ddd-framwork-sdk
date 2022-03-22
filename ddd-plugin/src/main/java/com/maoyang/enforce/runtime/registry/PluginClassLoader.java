/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.runtime.registry;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Plugin类加载器.
 */
@Slf4j
final class PluginClassLoader extends URLClassLoader {
    private static final String dddPackage = "com.maoyang.enforce";

    private final ClassLoader jdkClassLoader;
    private final ClassLoader containerClassLoader;

    PluginClassLoader(URL[] urls, ClassLoader jdkClassLoader, ClassLoader containerClassLoader) {
        super(urls);

        for (URL url : urls) {
            addUrl(url); // the classpath
        }

        this.jdkClassLoader = jdkClassLoader;
        this.containerClassLoader = containerClassLoader;
    }

    void addUrl(URL url) {
        super.addURL(url);
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Class clazz = this.findLoadedClass(className);
        if (clazz != null) {
            // 如果类已经加载过，就返回那个已经加载好的类
            return clazz;
        }

        // 如果这个类是JDK自己的，就用 JDKClassLoader 加载
        try {
            clazz = jdkClassLoader.loadClass(className);
            if (clazz != null) {
                // 说明该类是JRE的类
                log.debug("loaded {} with {}", className, jdkClassLoader);
                return clazz;
            }
        } catch (ClassNotFoundException ignored) {
        }

        // 不是JDK本身的类
        if (containerFirstClass(className)) {
            clazz = containerClassLoader.loadClass(className);
            if (clazz != null) {
                log.debug("loaded {} with {}", className, containerClassLoader);
                return clazz;
            }
        }

        // Plugin加载器自己加载
        try {
            // look for classes in the file system(jar)
            clazz = this.findClass(className);
            if (clazz != null) {
                log.info("loaded {} with {}", className, this);
                if (resolve) {
                    resolveClass(clazz);
                }
                return clazz;
            }
        } catch (ClassNotFoundException ignored) {
        }

        // 如果Plugin加载器无法加载，fallback to 中台Container加载器
        if (clazz == null) {
            clazz = containerClassLoader.loadClass(className); // might throw ClassNotFoundException
            if (clazz != null) {
                log.debug("loaded {} with {}", className, containerClassLoader);
                return clazz;
            }
        }

        // null
        return clazz;
    }

    // 中台Container优先加载的类
    boolean containerFirstClass(String className) {
        return className != null && className.startsWith(dddPackage);
    }
}
