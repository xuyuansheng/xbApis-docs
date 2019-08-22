package cn.xuxiaobu.doc.apis.processor.config;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.processor.ApiDefinitionProcessor;

/**
 * 配置处理
 *
 * @author 020102
 * @date 2019-08-22 13:16
 */
public interface ApiConfigProcessor extends ApiDefinitionProcessor {

    /**
     * 处理API定义的注释
     * @param definition 被处理的API
     */
    void postConfigProcess(ApiDefinition definition);
}
