package cn.xuxiaobu.doc.apis.filter.java.methodfilter;

import cn.xuxiaobu.doc.apis.annotions.Apis;
import cn.xuxiaobu.doc.apis.filter.java.JavaApiFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author 020102
 * @date 2019-07-19 14:09
 */
public class JavaCommonMethodFilter implements JavaApiFilter<Method> {
    @Override
    public Boolean doFilter(Method method) {
        boolean result = method.isAnnotationPresent(Apis.class);
        if(result){
            return true;
        }
        Optional<Annotation> apis = Stream.of(method.getDeclaredAnnotations()).filter(an -> "Apis".equals(an.annotationType().getSimpleName()))
                .findAny();
        return apis.isPresent();
    }
}
