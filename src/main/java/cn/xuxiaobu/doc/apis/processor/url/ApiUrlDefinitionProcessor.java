package cn.xuxiaobu.doc.apis.processor.url;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.processor.ApiDefinitionProcessor;

/**
 * @author 020102
 * @date 2019-07-20 09:18
 */
public interface ApiUrlDefinitionProcessor extends ApiDefinitionProcessor {

    void postUrlProcess(ApiDefinition definition);

}
