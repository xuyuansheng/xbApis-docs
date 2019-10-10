package cn.xuxiaobu.doc.apis.definition;


import cn.xuxiaobu.doc.apis.enums.CustomTagType;
import cn.xuxiaobu.doc.util.processor.GenericityUtils;
import cn.xuxiaobu.doc.util.wrapper.WrapperUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 返回类型的显示结构定义
 *
 * @author 020102
 * @date 2019-08-01 13:54
 */
@Data
@Accessors(chain = true)
@Slf4j
public class ReturnTypeDefinition {


    public ReturnTypeDefinition() {
    }


    public void init(Type returnType, String returnDescription) {
        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(returnType);
            Class rawType = Class.class.cast(parameterizedType.getRawType());
            String index = rawType.getName();

            ClassOrInterfaceDeclaration clazzUnit = GenericityUtils.getClassOrInterfaceDeclaration(index);
            /* 获取到泛型和对应实际类型的对应关系 */
            Map<String, Type> genericity = GenericityUtils.getMethodTypeGenericity(clazzUnit, parameterizedType);
            /* 把泛型替换为实际的类型(虽然里面也有可能包含泛型) */
            returnType = GenericityUtils.genericityReplace(parameterizedType, genericity);

        }
        TypeWrapper realType = WrapperUtils.getInstance(returnType);
        this.returnType = realType;
        this.description = returnDescription;
        this.ifCollection = realType.ifArray();
        this.typeShowDefinition = initTypeShowDefinition();
    }


    /**
     * 实际的放回类型
     */
    @JSONField(serialize = false)
    private TypeWrapper returnType;
    /**
     * 返回结果的描述(注意:不是返回类型中各个字段含义的描述,而是对整个返回结果的描述)
     */
    private String description;

    /**
     * 是否为集合或数组
     */
    private boolean ifCollection;
    /**
     * 显示的信息
     */
    private TypeShowDefinition typeShowDefinition;

    /**
     * 获取method本身返回类型的注释
     *
     * @return
     */
    private TypeShowDefinition initTypeShowDefinition() {
        List<CustomTags> defaultValueList = CustomTagType.defaultValue.getTagFromString(this.description).orElse(null);
        String defaultValue = defaultValueList == null ? "" : defaultValueList.get(0).getValue();
        TypeShowDefinition typeShowDefinition = new TypeShowDefinition()
                .setName("result")
                .setCompleteTypeShow(returnType.getTypeName())
                .setReturnTypeShow(returnType.getSimpleName())
                .setDefaultValue(defaultValue)
                .setDescription(this.description)
                .setIfCollection(returnType.ifArrayOrCollection())
                .setBelongsToClassName(returnType.getCompleteClassName());
        if (!this.returnType.ifFinalType()) {
            typeShowDefinition.setFields(returnType.getFieldsTypeShowDefinition());
        }
        return typeShowDefinition;
    }

}
