package cn.xuxiaobu.doc.export.postman;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.definition.DefaultJavaApiDefinition;
import cn.xuxiaobu.doc.export.postman.collections.PostManRequest;
import cn.xuxiaobu.doc.export.postman.collections.PostManResponse;
import cn.xuxiaobu.doc.export.postman.groupby.GroupByClass;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 一个json文件
 *
 * @author 020102
 * @date 2019-09-25 14:19
 */
@Data
@Accessors(chain = true)
public class PostManApiJson {

    private PostManInfo info;

    private List<PostManCollections> item;


    public static PostManApiJson build(List<ApiDefinition> apiDefinitions) {
        PostManApiJson result = new PostManApiJson();
        /* 设置目录名称 */
        result.setInfo(new PostManInfo().setName("collectionsName"));
        /* 按类名分组 */
        Map<String, List<ApiDefinition>> groupByClass = apiDefinitions.stream().filter(api -> api instanceof DefaultJavaApiDefinition)
                .collect(Collectors.groupingBy(new GroupByClass()));
        /* 解析API */
        List<PostManCollections> itemTemp = groupByClass.entrySet().stream().map(apiEn -> {
            /* 分组目录名 */
            String collectionName = apiEn.getKey();
            /* 分组内的API */
            List<PostManCollections> itemT = getApiItem(apiEn.getValue());
            return new PostManCollections().setName(collectionName).setItem(itemT);
        }).collect(Collectors.toList());
        result.setItem(itemTemp);
        return result;
    }

    private static List<PostManCollections> getApiItem(List<ApiDefinition> apiDefinitions) {
        return apiDefinitions.stream()
//                    .map(api -> DefaultJavaApiDefinition.class.cast(api))
                .map(api -> {
                    /* 请求名取50个字符 */
                    String apiName = StringUtils.substring(api.getDefinitionName(), 0, 50);

                    return new PostManCollections()
                            .setName(apiName)
                            .setRequest(PostManRequest.getPostManRequest(api))
                            .setResponse(PostManResponse.getPostManResponse());
                }).collect(Collectors.toList());
    }

}
