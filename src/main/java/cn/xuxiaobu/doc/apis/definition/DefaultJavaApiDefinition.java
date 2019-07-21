package cn.xuxiaobu.doc.apis.definition;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.core.io.Resource;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 默认API定义
 *
 * @author 020102
 * @date 2019-07-18 16:59
 */
@Data
@Accessors(chain = true)
public class DefaultJavaApiDefinition implements ApiDefinition {

    /**
     * API所属于的类元数据
     */
    private Class<?> clazzMateData;
    /**
     * Java源文件,从中提取注释的内容
     */
    private Resource javaFileMateData;
    /**
     * API的方法元数据
     */
    private Method methodMateData;

    /**
     * 协议
     */
    private String protocol = "http";
    /**
     * 域名
     */
    private String host = "localhost";
    /**
     * 端口
     */
    private String port = "80";
    /**
     * url,不包含域名和端口
     */
    private List<String> url;
    /**
     * API的描述
     */
    private String description;
    /**
     * API支持的类型
     */
    private List<String> method;
    /**
     * API参数
     */
    private Object param;
    /**
     * API结果
     */
    private Object result;

    @Override
    public String getAddress(Integer index) {
        int resultIndex = Math.max(0, Math.min(index, this.url.size() - 1));
        return this.protocol + "://" + this.host + ":" + this.port + "/" + this.url.get(resultIndex);
    }

    @Override
    public String getDefinitionName() {
        if(this.clazzMateData==null||this.methodMateData==null){
            return "";
        }
        return this.clazzMateData.getName()+" : "+this.methodMateData.getName();
    }

}