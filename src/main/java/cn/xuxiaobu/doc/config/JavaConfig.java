package cn.xuxiaobu.doc.config;

import cn.xuxiaobu.doc.apis.filter.java.JavaApiFilter;
import cn.xuxiaobu.doc.apis.processor.url.ApiUrlDefinitionProcessor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 所有配置类,如源码目录,class文件目录,依赖jar包目录等
 *
 * @author 020102
 * @date 2019-07-19 09:50
 */

@Data
@Accessors(chain = true)
public class JavaConfig {

    /**
     * 文档的标题
     */
    String title = "文档的标题";
    /**
     * 文档的版本
     */
    String version = "1.1.1";
    /**
     * 文档的描述,不是api的描述,不能弄混了
     */
    String description;

    /**
     * 源码目录
     */
    String sourceJavaDir;
    /**
     * class文件目录根目录,如:maven打包生成的classes目录
     */
    String sourceClassDir;
    /**
     * 项目依赖的源码jar包或文件夹
     */
    List<String> sourceDependencyJava;
    /**
     * 项目依赖的jar包或class文件根目录
     */
    List<String> sourceDependencyClass;
    /**
     * 文档输出目录
     */
    String outPutDir;
    /**
     * 协议
     */
    private String protocol;
    /**
     * 域名
     */
    private String host;
    /**
     * 端口
     */
    private String port;
    /**
     * 类和方法过滤器
     */
    List<JavaApiFilter> apiFilters;
    /**
     * Url处理器
     */
    List<ApiUrlDefinitionProcessor> urlDefinitionProcessors;

    public JavaConfig(String sourceJavaDir, String sourceClassDir) {
        this.sourceJavaDir = sourceJavaDir;
        this.sourceClassDir = sourceClassDir;
        this.sourceDependencyJava = null;
        this.sourceDependencyClass = null;
    }

    public JavaConfig(String sourceJavaDir, String sourceClassDir, List<String> sourceDependencyJava, List<String> sourceDependencyClass) {
        this.sourceJavaDir = sourceJavaDir;
        this.sourceClassDir = sourceClassDir;
        this.sourceDependencyJava = sourceDependencyJava;
        this.sourceDependencyClass = sourceDependencyClass;
    }


}
