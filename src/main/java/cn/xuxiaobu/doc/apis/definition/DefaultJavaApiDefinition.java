package cn.xuxiaobu.doc.apis.definition;

import cn.xuxiaobu.doc.apis.enums.JavaFrameworkType;
import com.alibaba.fastjson.annotation.JSONField;
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
public class DefaultJavaApiDefinition implements ConfigurableApiDefinition {


    public DefaultJavaApiDefinition() {
        /* 默认值 */
        this.protocol = "http";
        this.host = "localhost";
        this.port = "80";
        this.description = "";
        this.returnTypeDefinition = new ReturnTypeDefinition();
    }

    /**
     * API所属于的类元数据
     */
    @JSONField(serialize = false)
    private Class<?> clazzMateData;
    /**
     * Java源文件,从中提取注释的内容
     */
    @JSONField(serialize = false)
    private Resource javaFileMateData;
    /**
     * API的方法元数据
     */
    @JSONField(serialize = false)
    private Method methodMateData;
    /**
     * API的来源类型,
     * 1.从spring的RequestMapping,PostMapping等注解解析获取
     * 2.从自定义的注解Apis解析获取
     */
    private JavaFrameworkType definitionFrom;
    /**
     * 协议
     */
    private String protocol;
    /**
     * 域名
     */
    private String host;
    /**
     * 端口
     */
    private String port;
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
    private ReturnTypeDefinition returnTypeDefinition;



    @Override
    public String getAddress(Integer index) {
        int resultIndex = Math.max(0, Math.min(index, this.url.size() - 1));
        return this.protocol + "://" + this.host + ":" + this.port + "/" + this.url.get(resultIndex);
    }

    @Override
    public String getDefinitionName() {
        if (this.clazzMateData == null || this.methodMateData == null) {
            return "";
        }
        return this.clazzMateData.getName() + " : " + this.methodMateData.getName();
    }

}
