package cn.xuxiaobu.doc.util.processor;

import cn.xuxiaobu.doc.apis.annotions.Apis;
import cn.xuxiaobu.doc.apis.annotions.JdkDynamicProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 注解工具类
 *
 * @author 020102
 * @date 2019-08-21 16:44
 */
public class AnnotationUtils {


    /**
     * 获取类上的自定义注解
     * @param clazz
     * @return
     */
    public static Apis getApisAnnotation(Class<?> clazz){
        Apis urlAnnotation = clazz.getDeclaredAnnotation(Apis.class);
        if (urlAnnotation != null) {
            return urlAnnotation;
        }
        Optional<Annotation> apis = Stream.of(clazz.getDeclaredAnnotations()).filter(an -> "Apis".equals(an.annotationType().getSimpleName()))
                .findAny();
        if(apis.isPresent()){
            Annotation api = apis.get();
            return new JdkDynamicProxy(api).getProxy();
        }
        return null;
    }

    /**
     * 获取方法上的自定义注解
     * @param method
     * @return
     */
    public static Apis getApisAnnotation(Method method){
        Apis urlAnnotation = method.getDeclaredAnnotation(Apis.class);
        if (urlAnnotation != null) {
            return urlAnnotation;
        }
        Optional<Annotation> apis = Stream.of(method.getDeclaredAnnotations()).filter(an -> "Apis".equals(an.annotationType().getSimpleName()))
                .findAny();
        if(apis.isPresent()){
            Annotation api = apis.get();
            return new JdkDynamicProxy(api).getProxy();
        }
        return null;
    }

}
