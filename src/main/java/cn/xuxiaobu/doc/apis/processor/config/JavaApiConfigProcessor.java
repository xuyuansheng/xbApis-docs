package cn.xuxiaobu.doc.apis.processor.config;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.definition.ConfigurableApiDefinition;
import cn.xuxiaobu.doc.config.JavaConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 020102
 * @date 2019-08-22 13:18
 */
@Slf4j
public class JavaApiConfigProcessor implements ApiConfigProcessor {

    private JavaConfig javaConfig;

    public JavaApiConfigProcessor(JavaConfig javaConfig) {
        this.javaConfig = javaConfig;
    }

    @Override
    public void postConfigProcess(ApiDefinition definition) {
        if(!(definition instanceof ConfigurableApiDefinition)){
            return;
        }
        ConfigurableApiDefinition realDefinition = ConfigurableApiDefinition.class.cast(definition);
        realDefinition
                .setProtocol(javaConfig.getProtocol())
                .setPort(javaConfig.getPort())
                .setHost(javaConfig.getHost());
    }
}
