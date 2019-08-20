package cn.xuxiaobu.doc.apis.definition;

import cn.xuxiaobu.doc.apis.enums.JavaFrameworkType;
import org.springframework.core.io.Resource;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 可配置的API定义
 *
 * @author 020102
 * @date 2019-08-20 09:33
 */
public interface ConfigurableApiDefinition extends ApiDefinition {
    /**
     * 设置域名
     *
     * @param host 域名
     * @return
     */
    ApiDefinition setHost(String host);

    /**
     * 设置端口
     *
     * @param port 端口
     * @return
     */
    ApiDefinition setPort(String port);

    /**
     * 设置URL,API的URL部分,不包含域名端口等
     *
     * @param urls url列表
     * @return
     */
    ApiDefinition setUrl(List<String> urls);

    /**
     * 设置接口描述
     *
     * @param description
     * @return
     */
    ApiDefinition setDescription(String description);

    /**
     * 设置API来源数据
     *
     * @param definitionType
     * @return
     */
    ApiDefinition setDefinitionFrom(JavaFrameworkType definitionType);


    /**
     * 设置API支持的请求方式,如:GET,POST等
     *
     * @param method
     * @return
     */
    ApiDefinition setMethod(List<String> method);

    /**
     * 设置类元数据
     *
     * @param clazzMateData
     * @return
     */
    ApiDefinition setClazzMateData(Class<?> clazzMateData);

    /**
     * 设置API源码数据
     *
     * @param javaFileMateData
     * @return
     */
    ApiDefinition setJavaFileMateData(Resource javaFileMateData);

    /**
     * 设置API方法元数据
     *
     * @param methodMateData API的方法
     * @return
     */
    ApiDefinition setMethodMateData(Method methodMateData);

    /**
     * 设置API支持的协议
     *
     * @param protocol 协议名
     * @return API
     */
    ApiDefinition setProtocol(String protocol);

    /**
     * 设置API的参数
     *
     * @param param 参数
     * @return API
     */
    ApiDefinition setParam(Object param);

    /**
     * 设置API的返回定义
     *
     * @param returnTypeDefinition API定义
     * @return API
     */
    ApiDefinition setReturnTypeDefinition(ReturnTypeDefinition returnTypeDefinition);

}
