package cn.xuxiaobu.doc.export.postman.collections.request;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 请求头
 *
 * @author 020102
 * @date 2019-09-25 14:42
 */
@Data
@Accessors(chain = true)
public class PostManHeader {

    private String key;
    private String name;
    private String value;
    private String type;

    public static List<PostManHeader> getPostManHeader(ApiDefinition apiDefinition) {
        return Stream.of(new PostManHeader()
                .setKey("Content-Type")
                .setName("Content-Type")
                .setValue("application/x-www-form-urlencoded")
                .setType("text")).collect(Collectors.toList());
    }

}
