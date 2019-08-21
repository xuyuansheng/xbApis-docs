package cn.xuxiaobu.doc.apis.parser;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.definition.DefaultJavaApiDefinition;
import cn.xuxiaobu.doc.apis.enums.JavaFrameworkType;
import cn.xuxiaobu.doc.apis.filter.java.clazzfilter.JavaSpringControllerFilter;
import cn.xuxiaobu.doc.apis.filter.java.methodfilter.JavaCommonMethodFilter;
import cn.xuxiaobu.doc.apis.filter.java.methodfilter.JavaSpringMethodFilter;
import cn.xuxiaobu.doc.apis.initialization.SourceFile;
import org.springframework.core.io.Resource;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 020102
 * @date 2019-07-19 14:03
 */
public class JavaApiParser implements ApiParser {
    /**
     * Java源码数据
     */
    SourceFile root;

    public JavaApiParser(SourceFile root) {
        this.root = root;
    }

    @Override
    public List<ApiDefinition> parse(Class<?> clazz) {
        Resource javaSourceFile = root.getResource(clazz.getName());
        List<ApiDefinition> methods = Stream.of(clazz.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(m ->{
                    final JavaFrameworkType javaFrameworkType = doGetDefinitionFrom(clazz,m);
                    return   new DefaultJavaApiDefinition()
                            .setClazzMateData(clazz)
                            .setDefinitionFrom(javaFrameworkType)
                            .setMethodMateData(m)
                            .setJavaFileMateData(javaSourceFile);
                }).collect(Collectors.toList());
        return methods;
    }

    /**
     * 获取Java项目定义来源
     *
     * @param clazz  方法所属的类
     * @param method 方法
     * @return
     */
    private JavaFrameworkType doGetDefinitionFrom(Class<?> clazz, Method method) {
        if (new JavaSpringControllerFilter().doFilter(clazz)&&new JavaSpringMethodFilter().doFilter(method)) {
            return JavaFrameworkType.SPRING_JAVA;
        } else if (new JavaCommonMethodFilter().doFilter(method)) {
            return JavaFrameworkType.COMMON_JAVA;
        } else {
            return JavaFrameworkType.UNKNOWN;
        }
    }


}
