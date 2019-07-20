package cn.xuxiaobu.doc;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.filter.java.clazzfilter.JavaSpringControllerFilter;
import cn.xuxiaobu.doc.apis.parser.JavaApiParser;
import cn.xuxiaobu.doc.apis.processor.url.JavaSpringUrlProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 020102
 * @date 2019-07-19 10:05
 */
public class JavaProcessSynopsisMaven extends JavaProcessSynopsis {


    public JavaProcessSynopsisMaven(JavaConfig javaConfig) {
        super(javaConfig);
    }

    @Override
    protected void apiDefinitionProcess() {
        super.apiDefinitions.stream().forEach(dfn -> {
            new JavaSpringUrlProcessor().postUrlProcess(dfn);
        });
    }

    @Override
    protected void getApiMetadata() {
        Optional<List<ApiDefinition>> definitionList = super.apiJavaNames.stream().map(k -> {
            JavaApiParser parse = new JavaApiParser(super.javaDependencySourceFileContext.getRoot(), super.urlClassLoader);
            List<ApiDefinition> res = parse.parse(k);
            return res;
        }).filter(f -> f != null).reduce((r1, r2) -> {
            r1.addAll(r2);
            return r1;
        });
        super.apiDefinitions = definitionList.orElse(new ArrayList<>(0));
    }

    @Override
    protected void filterJavaApiNames() {
        List<String> names = javaSourceFileContext.getJavaFileNames();
        super.apiJavaNames = names.stream().filter(new JavaSpringControllerFilter(super.urlClassLoader)::doFilter).collect(Collectors.toList());


    }


}
