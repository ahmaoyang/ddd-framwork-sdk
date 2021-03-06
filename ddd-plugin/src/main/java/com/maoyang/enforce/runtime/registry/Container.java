/*
 *
 *
 * @author maoyang
 */
package com.maoyang.enforce.runtime.registry;

import com.maoyang.enforce.annotation.Partner;
import com.maoyang.enforce.plugin.IContainerContext;
import com.maoyang.enforce.plugin.IPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 业务容器，用于动态加载个性化业务包：Plugin Jar.
 * Container常驻内存， PluginJar动态加载：动静分离
 */
@Slf4j
public final class Container {
    private static final Container instance = new Container();

    private static final ClassLoader jdkClassLoader = initJDKClassLoader();
    private static final ClassLoader containerClassLoader = Container.class.getClassLoader();
    private static final ApplicationContext containerApplicationContext = DDDBootstrap.applicationContext();

    private static final Map<String, IPlugin> activePlugins = new HashMap<>();

    private Container() {
    }

    /**
     * 获取业务容器单例.
     */
    @NotNull
    public static Container getInstance() {
        return instance;
    }

    /**
     * 获取当前所有活跃的{@code Plugin}.
     *
     * @return key: Plugin code
     */
    @NotNull
    public Map<String, IPlugin> getActivePlugins() {
        return activePlugins;
    }

    /**
     * 加载业务前台jar包.
     *
     * @param code      {@link IPlugin#getCode()}
     * @param version   version of the jar
     * @param jarUrl    Plugin jar URL
     * @param useSpring jar包里是否需要Spring机制
     * @throws Throwable
     */
    public synchronized void loadPartnerPlugin(@NotNull String code, @NotNull String version, @NotNull URL jarUrl, boolean useSpring) throws Throwable {
        File localJar = createLocalFile(jarUrl);
        localJar.deleteOnExit();

        log.info("loadPartnerPlugin {} -> {}", jarUrl, localJar.getCanonicalPath());
        FileUtils.copyInputStreamToFile(jarUrl.openStream(), localJar);
        loadPartnerPlugin(code, version, localJar.getAbsolutePath(), useSpring);
    }

    /**
     * 加载业务前台jar包，支持定制IContainerContext的实现.
     *
     * 如果使用本动态加载，就不要maven里静态引入业务前台jar包依赖了.
     *
     * @param code      {@link IPlugin#getCode()}
     * @param version   version of the jar
     * @param jarPath   jar path
     * @param useSpring jar包里是否需要Spring机制
     * @param containerContext container context instance
     * @throws Throwable
     */
    public synchronized void loadPartnerPlugin(@NotNull String code, @NotNull String version, @NotNull String jarPath, boolean useSpring, IContainerContext containerContext) throws Throwable {
        if (!jarPath.endsWith(".jar")) {
            throw new IllegalArgumentException("Invalid jarPath: " + jarPath);
        }

        long t0 = System.nanoTime();
        log.warn("Loading partner:{} useSpring:{}", jarPath, useSpring);
        try {
            Plugin plugin = new Plugin(code, version, jdkClassLoader, containerClassLoader, containerApplicationContext);
            plugin.load(jarPath, useSpring, Partner.class, containerContext);

            Plugin pluginToDestroy = (Plugin) activePlugins.get(code);
            if (pluginToDestroy != null) {
                log.warn("to destroy partner:{} ver:{}", code, plugin.getVersion());
                pluginToDestroy.onDestroy();
            }

            activePlugins.put(plugin.getCode(), plugin); // old plugin will be GC'ed eventually

            log.warn("Loaded partner:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000);
        } catch (Throwable ex) {
            log.error("fails to load partner:{}, cost {}ms", jarPath, (System.nanoTime() - t0) / 1000_000, ex);

            throw ex;
        }
    }

        /**
         * 加载业务前台jar包，使用默认的IContainerContext实现.
         *
         * 如果使用本动态加载，就不要maven里静态引入业务前台jar包依赖了.
         *
         * @param code      {@link IPlugin#getCode()}
         * @param version   version of the jar
         * @param jarPath   jar path
         * @param useSpring jar包里是否需要Spring机制
         * @throws Throwable
         */
    public synchronized void loadPartnerPlugin(@NotNull String code, @NotNull String version, @NotNull String jarPath, boolean useSpring) throws Throwable {
        loadPartnerPlugin(code, version, jarPath, useSpring, new ContainerContext(containerApplicationContext));
    }

    File createLocalFile(@NotNull URL jarUrl) throws IOException {
        String prefix = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String suffix = jarUrl.getPath().substring(jarUrl.getPath().lastIndexOf("/") + 1);
        return File.createTempFile(prefix, "." + suffix);
    }

    /**
     * 初始化jdk类加载器
     * @return
     */
    private static ClassLoader initJDKClassLoader() {
        ClassLoader parent;
        for (parent = ClassLoader.getSystemClassLoader(); parent.getParent() != null; parent = parent.getParent()) {
        }

        List<URL> jdkUrls = new ArrayList<>(100);
        try {
            String javaHome = System.getProperty("java.home").replace(File.separator + "jre", "");
            URL[] urls = ((URLClassLoader) ClassLoader.getSystemClassLoader()).getURLs();
            for (URL url : urls) {
                if (url.getPath().startsWith(javaHome)) {
                    // 只找JDK本身的
                    jdkUrls.add(url);
                }
            }
        } catch (Throwable shouldNeverHappen) {
            log.error("JDKClassLoader", shouldNeverHappen);
        }

        return new URLClassLoader(jdkUrls.toArray(new URL[0]), parent);
    }
}
