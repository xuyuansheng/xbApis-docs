package cn.xuxiaobu.doc.apis.definition;

import cn.xuxiaobu.doc.apis.processor.note.TypeWrapper;
import cn.xuxiaobu.doc.util.wrapper.WrapperUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 返回类型的显示结构定义
 *
 * @author 020102
 * @date 2019-08-01 13:54
 */
@Data
@Accessors(chain = true)
public class ReturnTypeDefinition {


    public ReturnTypeDefinition() {
    }


    public void init(Type returnType, String description) {
        this.returnTypeShow = returnType.getTypeName();
        TypeWrapper realType = WrapperUtils.getInstance(returnType);
        this.returnType = realType;
        this.description = description;
        this.defaultValue = realType.getDefaultValue();
        this.map = realType.getFieldsType();
        this.ifCollection = realType.ifArray();
    }


    /**
     * 返回类型的显示
     */
    private String returnTypeShow;
    /**
     * 实际的放回类型
     */
    private TypeWrapper returnType;
    /**
     * 返回结果的描述(注意:不是返回类型中各个字段含义的描述,而是对整个返回结果的描述)
     */
    private String description;
    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 对象中的字段
     */
    private Map<String, TypeWrapper> map;
    /**
     * 是否为集合或数组
     */
    private boolean ifCollection;


}
