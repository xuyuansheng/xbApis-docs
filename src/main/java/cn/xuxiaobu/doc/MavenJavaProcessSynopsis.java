package cn.xuxiaobu.doc;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.definition.DefaultJavaApiDefinition;
import cn.xuxiaobu.doc.apis.filter.java.ChainFilterUtils;
import cn.xuxiaobu.doc.apis.filter.java.clazzfilter.JavaCommonClassFilter;
import cn.xuxiaobu.doc.apis.filter.java.clazzfilter.JavaSpringControllerFilter;
import cn.xuxiaobu.doc.apis.filter.java.methodfilter.JavaCommonMethodFilter;
import cn.xuxiaobu.doc.apis.filter.java.methodfilter.JavaSpringMethodFilter;
import cn.xuxiaobu.doc.apis.processor.note.JavaApiNoteProcessor;
import cn.xuxiaobu.doc.apis.processor.url.JavaUrlProcessorSupport;
import cn.xuxiaobu.doc.config.JavaConfig;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 020102
 * @date 2019-07-19 10:05
 */
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
            new JavaApiNoteProcessor().postNoteProcess(def);
        });
        /* 处理API描述等注释信息 */

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
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(n -> n != null)
                .filter(new ChainFilterUtils<Class<?>>(new JavaCommonClassFilter(), new JavaSpringControllerFilter())::doFilterOr)
                .collect(Collectors.toList());
    }
}
