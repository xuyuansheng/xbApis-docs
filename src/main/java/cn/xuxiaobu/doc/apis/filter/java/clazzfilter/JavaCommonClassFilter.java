package cn.xuxiaobu.doc.apis.filter.java.clazzfilter;

import cn.xuxiaobu.doc.apis.annotions.Apis;
import cn.xuxiaobu.doc.apis.filter.java.JavaApiFilter;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author 020102
 * @date 2019-07-19 14:09
 */
public class JavaCommonClassFilter implements JavaApiFilter<Class<?>> {

    @Override
    public Boolean doFilter(Class<?> target) {
        boolean result = target.isAnnotationPresent(Apis.class);
        if(result){
            return true;
        }
        Optional<Annotation> apis = Stream.of(target.getDeclaredAnnotations()).filter(an -> "Apis".equals(an.annotationType().getSimpleName()))
                .findAny();
        return apis.isPresent();
    }
}
