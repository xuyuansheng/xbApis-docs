package cn.xuxiaobu.doc.apis.definition;

import cn.xuxiaobu.doc.apis.enums.JavaFrameworkType;
import org.springframework.core.io.Resource;

import java.lang.reflect.Method;
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
     * 获取端口
     *
     * @return
     */
    String getPort();

    /**
     * 获取API支持的协议
     *
     * @return API协议
     */
    String getProtocol();

    /**
     * 获取URL,API的URL部分,不包含域名端口等
     *
     * @return
     */
    List<String> getUrl();

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
     * 获取API支持的请求方式,如:GET,POST等
     *
     * @return
     */
    List<String> getMethod();

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
     * 获取API所属类的的元数据
     *
     * @return
     */
    Class<?> getClazzMateData();

    /**
     * 获取API源码数据(即:jar包的inputStream)
     *
     * @return
     */
    Resource getJavaFileMateData();

    /**
     * 获取API方法的元数据
     *
     * @return
     */
    Method getMethodMateData();

    /**
     * 获取参数数据
     *
     * @return
     */
    Object getParam();

    /**
     * 获取返回信息数据
     *
     * @return
     */
    ReturnTypeDefinition getReturnTypeDefinition();

}
