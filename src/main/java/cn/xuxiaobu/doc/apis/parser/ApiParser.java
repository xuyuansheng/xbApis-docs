package cn.xuxiaobu.doc.apis.parser;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;

import java.util.List;

/**
 * API解析器,从Java类中解析出来其中的API数据,转换为ApiDefinition
 *
 * @author 020102
 * @date 2019-07-19 13:54
 */
@FunctionalInterface
public interface ApiParser {
    /**
     * 把class转换为 ApiDefinition
     * @see ApiDefinition
     * @param name 被转换的clazz类
     * @return API定义
     */
    List<ApiDefinition> parse(Class<?> name);
}
