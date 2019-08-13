package cn.xuxiaobu.doc.apis.definition;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @program: xbapi-docs
 * @description: 类型显示定义
 * @author: Mr.Xu
 * @create: 2019/8/9 21:46
 */
@Data
@Accessors(chain = true)
public class TypeShowDefinition {

    /**
     * 名称
     */
    private String name;
    /**
     * 返回类型的显示
     */
    private String returnTypeShow;
    /**
     * 完整形式类型显示
     */
    private String completeTypeShow;

    /**
     * 返回结果类型的描述
     */
    private String description;
    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 是否为集合或数组
     */
    private boolean ifCollection;

    /**
     * 这个字段所属于类的全类名,用于获取java源文件,从而获取到注释等内容
     */
    private String belongsToClassName;

    private List<TypeShowDefinition> fields;


}
