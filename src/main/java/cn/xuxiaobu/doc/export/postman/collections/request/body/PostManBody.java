package cn.xuxiaobu.doc.export.postman.collections.request.body;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.export.postman.collections.request.body.paramtype.KeyValueParam;
import cn.xuxiaobu.doc.export.postman.collections.request.body.paramtype.RawParam;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 键值对参数形式
 *
 * @author 020102
 * @date 2019-09-25 16:01
 */
@Data
@Accessors(chain = true)
public class PostManBody {
    /**
     * formdata urlencoded raw
     * 每一个值对应不通的模式
     */
    private String mode;

    private List<KeyValueParam> formdata;
    private List<KeyValueParam> urlencoded;
    private String raw;
    private RawParam option;

    public static PostManBody getPostManBody(ApiDefinition apiDefinition, String method) {

        PostManBody urlEncoded = new PostManBody().setMode("urlencoded");

        if (!StringUtils.equalsIgnoreCase(method, RequestMethod.GET.name())) {
            List<KeyValueParam> queryList = apiDefinition.getParam().stream().map(param ->
                    new KeyValueParam().setKey(param.getName())
                            .setValue(param.getDefaultValue())
                            .setDescription(param.getDescription())
            ).collect(Collectors.toList());
            urlEncoded.setUrlencoded(queryList);
        } else {
            urlEncoded.setUrlencoded(new ArrayList<>(0));
        }
        return urlEncoded;
    }
}