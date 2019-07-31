package cn.xuxiaobu.doc.apis.processor.note;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.processor.ApiDefinitionProcessor;

/**
 * @author 020102
 * @date 2019-07-20 09:18
 */
public interface ApiNoteProcessor extends ApiDefinitionProcessor {
    /**
     * 处理API定义的注释
     * @param definition 被处理的API
     */
    void postNoteProcess(ApiDefinition definition);

}
