package cn.xuxiaobu.doc.export.postman.collections;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.export.postman.collections.request.PostManHeader;
import cn.xuxiaobu.doc.export.postman.collections.request.PostManUrl;
import cn.xuxiaobu.doc.export.postman.collections.request.body.PostManBody;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 请求
 *
 * @author 020102
 * @date 2019-09-25 14:41
 */
@Data
@Accessors(chain = true)
public class PostManRequest {

    private String method;
    private String description;
    private List<PostManHeader> header;
    private PostManBody body;
    private PostManUrl url;

    public static PostManRequest getPostManRequest(ApiDefinition apiDefinition) {
        /* 有 POST 取 POST 没有取第一个 */
        String methodName = apiDefinition.getMethod().stream()
                .filter(api -> StringUtils.equalsIgnoreCase(RequestMethod.POST.name(), api))
                .findFirst().orElse(apiDefinition.getMethod().get(0));

        return new PostManRequest()
                .setBody(PostManBody.getPostManBody(apiDefinition, methodName))
                .setHeader(PostManHeader.getPostManHeader(apiDefinition))
                .setUrl(PostManUrl.getPostManUrl(apiDefinition, methodName))
                .setMethod(methodName)
                .setDescription(apiDefinition.getDescription());
    }

}
