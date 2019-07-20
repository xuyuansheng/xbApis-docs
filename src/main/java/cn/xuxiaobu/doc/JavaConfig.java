package cn.xuxiaobu.doc;

import lombok.Data;

import java.util.List;

/**
 * 所有配置类,如源码目录,class文件目录,依赖jar包目录等
 *
 * @author 020102
 * @date 2019-07-19 09:50
 */

@Data
public class JavaConfig {

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
