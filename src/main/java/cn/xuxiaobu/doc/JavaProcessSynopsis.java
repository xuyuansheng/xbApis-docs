package cn.xuxiaobu.doc;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.initialization.JavaFileInitialization;
import cn.xuxiaobu.doc.apis.initialization.JavaSourceFileContext;
import cn.xuxiaobu.doc.exceptions.InitSourceException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 流程大纲
 *
 * @author 020102
 * @date 2019-07-19 09:24
 */
public abstract class JavaProcessSynopsis {
    /**
     * 环境配置
     */
    protected JavaConfig javaConfig;
    /**
     * 源码环境
     */
    protected JavaSourceFileContext javaSourceFileContext;
    /**
     * 依赖jar的源码
     */
    protected JavaSourceFileContext javaDependencySourceFileContext;
    /**
     * 所有的class
     */
    protected URLClassLoader urlClassLoader;
    /**
     * 所有可能存在API的类的全类名
     */
    protected List<String> apiJavaNames;

    protected List<ApiDefinition> apiDefinitions;

    final public void buildDoc() {

        /* 1.准备Java源文件,不包括依赖的jar包源码 */
        loadJavaSource();
        /* 2.准备Java依赖的jar包源码,和第一步的Java源码 */
        loadDependencyJavaSource();
        /* 3.准备所有的class文件,包括源码编译生成的class和项目依赖的所有class */
        loadDependencyClassSource();
        /* 4.提取所有可能存在API的类名,比如:在类上定义了Controller注解,但是没有方法上有RequestMapping等注解的类也会包含进来,如: com.java.controller.TestController */
        filterJavaApiNames();
        /* 5.获取所有的API定义元数据,把上一步可能不存在API的类都会排除 */
        getApiMetadata();
        /* 6.处理API元数据,得到全部的定义 */
        apiDefinitionProcess();
    }

    public JavaProcessSynopsis(JavaConfig javaConfig) {
        this.javaConfig = javaConfig;
    }

    protected void loadJavaSource() {
        String source = this.javaConfig.getSourceJavaDir();
        try {
            File sourceFile = new File(source);
            if (!sourceFile.exists()) {
                throw new InitSourceException(source);
            }
            URL url = sourceFile.toURI().toURL();
            this.javaSourceFileContext = new JavaSourceFileContext(new JavaFileInitialization(), Stream.of(url).collect(Collectors.toList()));
        } catch (MalformedURLException e) {
            throw new InitSourceException(source, e);
        }
    }

    protected void loadDependencyJavaSource() {
        List<String> source = this.javaConfig.getSourceDependencyJava();
        if (source == null) {
            this.javaDependencySourceFileContext = this.javaSourceFileContext;
            return;
        }
        source.add(this.javaConfig.getSourceJavaDir());
        List<URL> urls = source.stream().map(s -> {
            try {
                URL sourceFile = new File(s).toURI().toURL();
                return sourceFile;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
        this.javaDependencySourceFileContext = new JavaSourceFileContext(new JavaFileInitialization(), urls);
    }

    protected void loadDependencyClassSource() {
        String sourceClass = this.javaConfig.getSourceClassDir();
        List<String> dependencyClass = this.javaConfig.getSourceDependencyClass();
        if (dependencyClass == null) {
            dependencyClass = Stream.of(sourceClass).collect(Collectors.toList());
        } else {
            dependencyClass.add(sourceClass);
        }
        List<URL> urls = dependencyClass.stream().map(s -> {
            try {
                URL url = new File(s).toURI().toURL();
                return url;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
        URL[] urlArray = urls.stream().toArray(k -> new URL[urls.size()]);
        this.urlClassLoader = new URLClassLoader(urlArray, Thread.currentThread().getContextClassLoader());
    }

    protected abstract void filterJavaApiNames();

    protected abstract void getApiMetadata();

    protected abstract void apiDefinitionProcess();


}
