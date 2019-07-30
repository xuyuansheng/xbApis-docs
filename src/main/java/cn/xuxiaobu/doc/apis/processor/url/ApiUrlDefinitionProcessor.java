package cn.xuxiaobu.doc.apis.processor.url;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.processor.ApiDefinitionProcessor;

/**
 * @author 020102
 * @date 2019-07-20 09:18
 */
public interface ApiUrlDefinitionProcessor extends ApiDefinitionProcessor {
    /**
     * 处理API定义的URL和支持的请求方式
     * @param definition 被处理的API
     */
    void postUrlProcess(ApiDefinition definition);

}
