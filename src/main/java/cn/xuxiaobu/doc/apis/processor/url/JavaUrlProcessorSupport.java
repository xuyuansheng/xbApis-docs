package cn.xuxiaobu.doc.apis.processor.url;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.enums.JavaFrameworkType;

import java.util.List;

/**
 * URL处理
 *
 * @author 020102
 * @date 2019-07-30 15:42
 */
public class JavaUrlProcessorSupport {

    public static void doUrlProcess(List<ApiDefinition> apiDefinitions){
        /* 处理URL和支持的请求方式 */
        apiDefinitions.stream().filter(k->k.getDefinitionFrom().equals(JavaFrameworkType.SPRING_JAVA)).forEach(dfn -> {
            new JavaSpringUrlProcessor().postUrlProcess(dfn);
        });
        apiDefinitions.stream().filter(k->k.getDefinitionFrom().equals(JavaFrameworkType.COMMON_JAVA)).forEach(dfn -> {
            new JavaCommonUrlProcessor().postUrlProcess(dfn);
        });
    }

}
