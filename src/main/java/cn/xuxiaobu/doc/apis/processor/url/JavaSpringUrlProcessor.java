package cn.xuxiaobu.doc.apis.processor.url;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.definition.DefaultJavaApiDefinition;
import cn.xuxiaobu.doc.exceptions.ProcessApiDefinitionException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 020102
 * @date 2019-07-20 09:11
 */
public class JavaSpringUrlProcessor implements ApiUrlDefinitionProcessor {
    /** 存放url的map集合key  */
    final String urlKey = "url";
    /**  存放method的map集合key */
    final String methodKey = "method";


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

        Method methodMateData = defaultJavaApiDefinition.getMethodMateData();
        RequestMapping request = methodMateData.getDeclaredAnnotation(RequestMapping.class);
        Map<String, List<String>> map = null;
        if (request != null) {
            map = getUrlAndSupportedMethod(request);
        } else {
            Optional<Annotation> mapping = Stream.of(methodMateData.getDeclaredAnnotations()).filter(k -> k instanceof PostMapping || k instanceof GetMapping).findFirst();
            if (mapping.isPresent()) {
                map = mapping.get() instanceof PostMapping ? getUrlAndSupportedMethod((PostMapping) mapping.get()) : getUrlAndSupportedMethod((GetMapping) mapping.get());
            } else {
                throw new ProcessApiDefinitionException(definition.getDefinitionName());
            }
        }

        RequestMapping rootUrlAnno = defaultJavaApiDefinition.getClazzMateData().getDeclaredAnnotation(RequestMapping.class);
        Map<String, List<String>> rootMap = Optional.ofNullable(rootUrlAnno).map(k -> getUrlAndSupportedMethod(k)).orElse(null);
        if (rootMap != null) {
            map.get(methodKey).addAll(rootMap.get(methodKey));
            ArrayList<String> temp = new ArrayList<>();
            for (String rm : rootMap.get(urlKey)) {
                for (String m : map.get(urlKey)) {
                    temp.add(rm + m);
                }
            }
            map.put(urlKey, temp);
        }
        if (map.get(methodKey).isEmpty()) {
            /** 如果都没有直接定义支持的请求方式,就默认支持get和post */
            map.put(methodKey, Stream.of(RequestMethod.POST.name(), RequestMethod.GET.name()).collect(Collectors.toList()));
        }
        defaultJavaApiDefinition.setMethod(map.get(methodKey));
        defaultJavaApiDefinition.setUrl(map.get(urlKey));
    }


    private Map<String, List<String>> getUrlAndSupportedMethod(RequestMapping requestMapping) {
        Map map = new HashMap(2);
        String[] subUrl = Optional.ofNullable(requestMapping).map(r -> ArrayUtils.addAll(r.value(), r.path())).get();
        List<String> subUrlList = Stream.of(subUrl).map(m -> StringUtils.startsWith(m, "/") || m.isEmpty() ? m : "/" + m).collect(Collectors.toList());
        List<String> supportMethod = Stream.of(requestMapping.method()).map(m -> m.name()).collect(Collectors.toList());
        map.put(urlKey, subUrlList);
        map.put(methodKey, supportMethod);
        return map;
    }


    private Map<String, List<String>> getUrlAndSupportedMethod(PostMapping postMapping) {
        Map map = new HashMap(2);
        String[] subUrl = Optional.ofNullable(postMapping).map(r -> ArrayUtils.addAll(r.value(), r.path())).get();
        List<String> subUrlList = Stream.of(subUrl).map(m -> StringUtils.startsWith(m, "/") || m.isEmpty() ? m : "/" + m).collect(Collectors.toList());
        List<String> supportMethod = Stream.of(RequestMethod.POST.name()).collect(Collectors.toList());
        map.put(urlKey, subUrlList);
        map.put(methodKey, supportMethod);
        return map;
    }

    private Map<String, List<String>> getUrlAndSupportedMethod(GetMapping getMapping) {
        Map map = new HashMap(2);
        String[] subUrl = Optional.ofNullable(getMapping).map(r -> ArrayUtils.addAll(r.value(), r.path())).get();
        List<String> subUrlList = Stream.of(subUrl).map(m -> StringUtils.startsWith(m, "/") || m.isEmpty() ? m : "/" + m).collect(Collectors.toList());
        List<String> supportMethod = Stream.of(RequestMethod.GET.name()).collect(Collectors.toList());
        map.put(urlKey, subUrlList);
        map.put(methodKey, supportMethod);
        return map;
    }

}
