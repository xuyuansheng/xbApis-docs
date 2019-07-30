package cn.xuxiaobu.doc.apis.parser;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.definition.DefaultJavaApiDefinition;
import cn.xuxiaobu.doc.apis.enums.JavaType;
import cn.xuxiaobu.doc.apis.filter.java.clazzfilter.JavaCommonClassFilter;
import cn.xuxiaobu.doc.apis.filter.java.clazzfilter.JavaSpringControllerFilter;
import cn.xuxiaobu.doc.apis.initialization.SourceFile;
import org.springframework.core.io.Resource;

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
        final JavaType javaType = doGetDefinitionFrom(clazz);
        List<ApiDefinition> methods = Stream.of(clazz.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(m ->
                        new DefaultJavaApiDefinition()
                                .setClazzMateData(clazz)
                                .setDefinitionFrom(javaType)
                                .setMethodMateData(m)
                                .setJavaFileMateData(javaSourceFile)
                ).collect(Collectors.toList());
        return methods;
    }

    /**
     * 获取Java项目定义来源
     *
     * @param clazz
     * @return
     */
    private JavaType doGetDefinitionFrom(Class<?> clazz) {
        if (new JavaSpringControllerFilter().doFilter(clazz)) {
            return JavaType.SPRING_JAVA;
        } else if (new JavaCommonClassFilter().doFilter(clazz)) {
            return JavaType.COMMON_JAVA;
        } else {
            return JavaType.UNKNOWN;
        }
    }


}
