package cn.xuxiaobu.doc.apis.processor.url;

import cn.xuxiaobu.doc.apis.annotions.Apis;
import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.definition.DefaultJavaApiDefinition;
import cn.xuxiaobu.doc.util.processor.AnnotationUtils;
import cn.xuxiaobu.doc.util.processor.ProcessorUtils;

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
        Apis clazzApis = AnnotationUtils.getApisAnnotation(clazzData);
        Optional.of(clazzApis).ifPresent(api->{
            methodListResult.addAll(Stream.of(api.method()).collect(Collectors.toList()));
            url.append(ProcessorUtils.urlFormat(api.value()));
        });
        Method methodMateData = defaultJavaApiDefinition.getMethodMateData();
        Apis methodApis = AnnotationUtils.getApisAnnotation(methodMateData);
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

}
