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
    ConfigurableApiDefinition setHost(String host);

    /**
     * 设置端口
     *
     * @param port 端口
     * @return
     */
    ConfigurableApiDefinition setPort(String port);

    /**
     * 设置URL,API的URL部分,不包含域名端口等
     *
     * @param urls url列表
     * @return
     */
    ConfigurableApiDefinition setUrl(List<String> urls);

    /**
     * 设置接口描述
     *
     * @param description
     * @return
     */
    ConfigurableApiDefinition setDescription(String description);

    /**
     * 设置API来源数据
     *
     * @param definitionType
     * @return
     */
    ConfigurableApiDefinition setDefinitionFrom(JavaFrameworkType definitionType);


    /**
     * 设置API支持的请求方式,如:GET,POST等
     *
     * @param method
     * @return
     */
    ConfigurableApiDefinition setMethod(List<String> method);

    /**
     * 设置类元数据
     *
     * @param clazzMateData
     * @return
     */
    ConfigurableApiDefinition setClazzMateData(Class<?> clazzMateData);

    /**
     * 设置API源码数据
     *
     * @param javaFileMateData
     * @return
     */
    ConfigurableApiDefinition setJavaFileMateData(Resource javaFileMateData);

    /**
     * 设置API方法元数据
     *
     * @param methodMateData API的方法
     * @return
     */
    ConfigurableApiDefinition setMethodMateData(Method methodMateData);

    /**
     * 设置API支持的协议
     *
     * @param protocol 协议名
     * @return API
     */
    ConfigurableApiDefinition setProtocol(String protocol);

    /**
     * 设置API的参数
     *
     * @param param 参数
     * @return API
     */
    ConfigurableApiDefinition setParam(List<TypeShowDefinition> param);

    /**
     * 设置API的返回定义
     *
     * @param returnTypeDefinition API定义
     * @return API
     */
    ConfigurableApiDefinition setReturnTypeDefinition(ReturnTypeDefinition returnTypeDefinition);

}
