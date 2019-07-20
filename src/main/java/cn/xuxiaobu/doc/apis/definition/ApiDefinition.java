package cn.xuxiaobu.doc.apis.definition;

import java.util.List;

/**
 * API的接口定义
 *
 * @author 020102
 * @date 2019-07-18 17:17
 */
public interface ApiDefinition {

    String getHost();

    ApiDefinition setHost(String host);

    String getPort();

    ApiDefinition setPort(String port);

    List<String> getUrl();

    ApiDefinition setUrl(List<String> urls);

    String getAddress(Integer index);

    String getDescription();

    ApiDefinition setDescription(String description);

    List<String> getMethod();

    ApiDefinition setMethod(List<String> method);

    String getDefinitionName();

}
