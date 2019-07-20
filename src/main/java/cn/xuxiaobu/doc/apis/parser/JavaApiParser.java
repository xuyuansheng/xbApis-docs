package cn.xuxiaobu.doc.apis.parser;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.definition.DefaultJavaApiDefinition;
import cn.xuxiaobu.doc.apis.filter.java.methodfilter.JavaSpringMethodFilter;
import org.springframework.core.io.Resource;

import java.lang.reflect.Modifier;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 020102
 * @date 2019-07-19 14:03
 */
public class JavaApiParser implements ApiParser {

    /** Java源码数据 */
    LinkedHashMap root;
    /** class数据 */
    URLClassLoader urlClassLoader;

    public JavaApiParser(LinkedHashMap root, URLClassLoader urlClassLoader) {
        this.root = root;
        this.urlClassLoader = urlClassLoader;
    }

    @Override
    public List<ApiDefinition> parse(String name) {
        try {
            Class<?> clazz = urlClassLoader.loadClass(name);
            Object javaSourceFile = root.get(name);
            Resource javaResource = javaSourceFile == null ? null : javaSourceFile instanceof Resource ? ((Resource) javaSourceFile) : null;
            List<ApiDefinition> methods = Stream.of(clazz.getDeclaredMethods())
                    .filter(m -> Modifier.isPublic(m.getModifiers()))
                    .filter(new JavaSpringMethodFilter()::doFilter)
                    .map(m ->
                         new DefaultJavaApiDefinition()
                        .setClazzMateData(clazz)
                        .setMethodMateData(m)
                        .setJavaFileMateData(javaResource)
                    ).collect(Collectors.toList());
            return methods;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
