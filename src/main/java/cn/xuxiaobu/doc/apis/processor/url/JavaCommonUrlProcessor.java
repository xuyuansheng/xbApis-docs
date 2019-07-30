package cn.xuxiaobu.doc.apis.processor.url;

import cn.xuxiaobu.doc.apis.annotions.Apis;
import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.definition.DefaultJavaApiDefinition;
import cn.xuxiaobu.doc.apis.processor.ProcessorUtils;
import com.alibaba.fastjson.JSON;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 020102
 * @date 2019-07-20 09:11
 */
public class JavaCommonUrlProcessor implements ApiUrlDefinitionProcessor {

    @Override
    public Integer getOrder() {
        return 1;
    }

    @Override
    public void postUrlProcess(ApiDefinition definition) {

        if (!(definition instanceof DefaultJavaApiDefinition)) {
            return;
        }
        DefaultJavaApiDefinition defaultJavaApiDefinition = (DefaultJavaApiDefinition) definition;
        /* api的请求方式 */
        List<String> methodListResult = new ArrayList<>(0);
        StringBuilder url = new StringBuilder();
        Class<?> clazzData = defaultJavaApiDefinition.getClazzMateData();
        Apis clazzApis = doGetAnnotion(clazzData);
        Optional.of(clazzApis).ifPresent(api->{
            methodListResult.addAll(Stream.of(api.method()).collect(Collectors.toList()));
            url.append(ProcessorUtils.urlFormat(api.value()));
        });
        Method methodMateData = defaultJavaApiDefinition.getMethodMateData();
        Apis methodApis = doGetAnnotion(methodMateData);
        Optional.of(methodApis).ifPresent(api->{
            methodListResult.addAll(Stream.of(api.method()).collect(Collectors.toList()));
            url.append(ProcessorUtils.urlFormat(api.value()));
        });
        if(methodListResult.isEmpty()){
            methodListResult.add("POST");
            methodListResult.add("GET");
        }
        defaultJavaApiDefinition.setMethod(methodListResult);
        defaultJavaApiDefinition.setUrl(Stream.of(url.toString()).collect(Collectors.toList()));
    }

    /**
     * 获取类上的自定义注解
     * @param clazz
     * @return
     */
    private Apis doGetAnnotion(Class<?> clazz){
        Apis urlAnnotation = clazz.getDeclaredAnnotation(Apis.class);
        if (urlAnnotation != null) {
            return urlAnnotation;
        }
        Optional<Annotation> apis = Stream.of(clazz.getDeclaredAnnotations()).filter(an -> "Apis".equals(an.annotationType().getSimpleName()))
                .findAny();
        if(apis.isPresent()){
            Annotation api = apis.get();
           return JSON.parseObject(JSON.toJSONString(api), Apis.class);
        }
        return null;
    }

    /**
     * 获取方法上的自定义注解
     * @param method
     * @return
     */
    private Apis doGetAnnotion(Method method){
        Apis urlAnnotation = method.getDeclaredAnnotation(Apis.class);
        if (urlAnnotation != null) {
            return urlAnnotation;
        }
        Optional<Annotation> apis = Stream.of(method.getDeclaredAnnotations()).filter(an -> "Apis".equals(an.annotationType().getSimpleName()))
                .findAny();
        if(apis.isPresent()){
            Annotation api = apis.get();
            return JSON.parseObject(JSON.toJSONString(api), Apis.class);
        }
        return null;
    }

}
