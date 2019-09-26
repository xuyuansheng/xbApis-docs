package cn.xuxiaobu.doc.export.postman.groupby;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

/**
 * 按类名分组
 *
 * @author 020102
 * @date 2019-09-25 16:28
 */
public class GroupByClass implements Function<ApiDefinition, String> {


    @Override
    public String apply(ApiDefinition s) {
        String[] split = StringUtils.split(s.getDefinitionName(), ":");
        if (ArrayUtils.isNotEmpty(split)) {
            return split[0].trim();
        }
        return "defaultCollections";
    }
}
