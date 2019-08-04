package cn.xuxiaobu.doc.apis.definition;

import cn.xuxiaobu.doc.apis.enums.JavaFrameworkType;

import java.util.List;

/**
 * API的接口定义
 *
 * @author 020102
 * @date 2019-07-18 17:17
 */
public interface ApiDefinition {
    /**
     * 获取域名
     *
     * @return
     */
    String getHost();

    /**
     * 设置域名
     *
     * @param host 域名
     * @return
     */
    ApiDefinition setHost(String host);

    /**
     * 获取端口
     *
     * @return
     */
    String getPort();

    /**
     * 设置端口
     *
     * @param port 端口
     * @return
     */
    ApiDefinition setPort(String port);

    /**
     * 获取URL,API的URL部分,不包含域名端口等
     *
     * @return
     */
    List<String> getUrl();

    /**
     * 设置URL,API的URL部分,不包含域名端口等
     *
     * @param urls url列表
     * @return
     */
    ApiDefinition setUrl(List<String> urls);

    /**
     * 获取API地址,地址可能有多个,所以传一个index表示获取第几个,如果index超出范围则就近取地址
     *
     * @param index 地址下标
     * @return
     */
    String getAddress(Integer index);

    /**
     * 获取接口描述
     *
     * @return
     */
    String getDescription();

    /**
     * 设置接口描述
     *
     * @param description
     * @return
     */
    ApiDefinition setDescription(String description);

    /**
     * 获取API支持的请求方式,如:GET,POST等
     *
     * @return
     */
    List<String> getMethod();

    /**
     * 设置API支持的请求方式,如:GET,POST等
     *
     * @param method
     * @return
     */
    ApiDefinition setMethod(List<String> method);

    /**
     * 获取API定义的名称
     *
     * @return
     */
    String getDefinitionName();

    /**
     * 获取API的来源类型,
     * 1.从spring的RequestMapping,PostMapping等注解解析获取
     * 2.从自定义的注解Apis解析获取
     *
     * @return
     */
    JavaFrameworkType getDefinitionFrom();

    /**
     * 设置API来源数据
     *
     * @param definitionType
     * @return
     */
    ApiDefinition setDefinitionFrom(JavaFrameworkType definitionType);

}
