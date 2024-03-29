package cn.xuxiaobu.doc;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.definition.DefaultJavaApiDefinition;
import cn.xuxiaobu.doc.apis.filter.java.ChainFilterUtils;
import cn.xuxiaobu.doc.apis.filter.java.clazzfilter.JavaCommonClassFilter;
import cn.xuxiaobu.doc.apis.filter.java.clazzfilter.JavaSpringControllerFilter;
import cn.xuxiaobu.doc.apis.filter.java.methodfilter.JavaCommonMethodFilter;
import cn.xuxiaobu.doc.apis.filter.java.methodfilter.JavaSpringMethodFilter;
import cn.xuxiaobu.doc.apis.processor.config.JavaApiConfigProcessor;
import cn.xuxiaobu.doc.apis.processor.note.JavaApiNoteProcessor;
import cn.xuxiaobu.doc.apis.processor.url.JavaUrlProcessorSupport;
import cn.xuxiaobu.doc.config.JavaConfig;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 020102
 * @date 2019-07-19 10:05
 */
@Slf4j
public class MavenJavaProcessSynopsis extends AbstractJavaProcessSynopsis {


    public MavenJavaProcessSynopsis(JavaConfig javaConfig) {
        super(javaConfig);
    }

    @Override
    protected void apiDefinitionProcess() {
        /* 处理URL和支持的请求方式 */
        JavaUrlProcessorSupport.doUrlProcess(super.apiDefinitions);
        /* 获取注释信息 */
        super.apiDefinitions.forEach(def->{
            new JavaApiNoteProcessor(super.javaDependencySourceFileContext).postNoteProcess(def);
            /* 处理API的协议,域名,端口等配置信息 */
            new JavaApiConfigProcessor(javaConfig).postConfigProcess(def);
        });

    }

    @Override
    protected void getApiMetadata() {

        List<ApiDefinition> definitions = super.doGetApiDefinition(super.apiJavaClasses, super.javaDependencySourceFileContext);

        List<ApiDefinition> definitionTemp = definitions.stream()
                .filter(d -> new ChainFilterUtils<Method>(new JavaCommonMethodFilter(), new JavaSpringMethodFilter()).doFilterOr(((DefaultJavaApiDefinition) d).getMethodMateData()))
                .collect(Collectors.toList());

        super.apiDefinitions = definitionTemp;
    }

    @Override
    protected void filterJavaApiNames() {
        List<String> names = javaSourceFileContext.getJavaFileNames();
        super.apiJavaClasses = names.stream()
                .map(m -> {
                    try {
                        return urlClassLoader.loadClass(m);
                    } catch (ClassNotFoundException e) {
                        log.error("加载不到该类 :",e);
                        log.info("加载不到该类 :",e.getMessage());
                        return null;
                    }
                })
                .filter(n -> n != null)
                .filter(new ChainFilterUtils<Class<?>>(new JavaCommonClassFilter(), new JavaSpringControllerFilter())::doFilterOr)
                .collect(Collectors.toList());
    }
}
