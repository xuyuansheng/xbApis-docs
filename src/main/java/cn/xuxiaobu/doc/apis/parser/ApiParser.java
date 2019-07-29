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
    List<ApiDefinition> parse(Class<?> name);
}
