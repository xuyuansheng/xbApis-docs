package cn.xuxiaobu.doc.export.postman.collections.request;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.export.postman.collections.request.body.paramtype.KeyValueParam;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * url
 *
 * @author 020102
 * @date 2019-09-25 14:42
 */
@Data
@Accessors(chain = true)
public class PostManUrl {

    private String raw;
    private String protocol;
    private List<String> host;
    private String port;
    private List<String> path;
    private List<KeyValueParam> query;

    public static PostManUrl getPostManUrl(ApiDefinition apiDefinition, String method) {
        /* 192.168.0.1  */
        List<String> hostList = Stream.of(StringUtils.split(apiDefinition.getHost(), ".")).collect(Collectors.toList());

        List<String> pathList = Stream.of(StringUtils.split(apiDefinition.getUrl().get(0), "/")).collect(Collectors.toList());

        PostManUrl resultUrl = new PostManUrl().setProtocol(apiDefinition.getProtocol())
                .setHost(hostList)
                .setPort(apiDefinition.getPort())
                .setPath(pathList)
                .setRaw(apiDefinition.getAddress(0));
        if (StringUtils.equalsIgnoreCase(method, RequestMethod.GET.name())) {
            List<KeyValueParam> queryList = apiDefinition.getParam().stream().map(param ->
                    new KeyValueParam().setKey(param.getName())
                            .setValue(param.getDefaultValue())
                            .setDescription(param.getDescription())
            ).collect(Collectors.toList());
            resultUrl.setQuery(queryList);
        }
        return resultUrl;
    }

}
