package cn.xuxiaobu.doc.util.wrapper;

import cn.xuxiaobu.doc.apis.definition.TypeShowDefinition;
import cn.xuxiaobu.doc.apis.definition.TypeWrapper;
import cn.xuxiaobu.doc.apis.processor.note.JavaFieldsVisitor;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: xbapi-docs
 * @description: GenericArrayType包装类
 * @author: Mr.Xu
 * @create: 2019/8/4 10:23
 */
@Data
@Accessors(chain = true)
public class GenericArrayTypeWrapper implements TypeWrapper {


    private GenericArrayType type;

    public GenericArrayTypeWrapper(GenericArrayType type) {
        this.type = type;
    }

    @Override
    public String getCompleteClassName() {
        return this.type.getGenericComponentType().getTypeName();
    }

    @Override
    public Map<String, TypeWrapper> getFieldsType() {
        return WrapperUtils.getInstance(this.type.getGenericComponentType()).getFieldsType();
    }

    @Override
    public List<TypeShowDefinition> getFieldsTypeShowDefinition() {
        /* 实际情况进不来此方法,即使进来了也是返回null */
        return null;
    }

    @Override
    public TypeShowDefinition getFieldTypeShowDefinition(String name, ClassOrInterfaceDeclaration parentClazz, Map<String, Type> genericitys) {
        /* 因为是泛型,所以可能有上层传过来的实际类型 */
        String typeName = this.type.getTypeName().replace("[]", "");
        Type typeReal = genericitys.get(typeName);
        typeReal = typeReal == null ? this.type : typeReal;
        TypeWrapper realType = WrapperUtils.getInstance(typeReal);

        if (typeReal instanceof ParameterizedType) {
            return WrapperUtils.getInstance(typeReal).getFieldTypeShowDefinition(name, parentClazz, new HashMap<>(0));
        }

        TypeShowDefinition def = new TypeShowDefinition()
                .setName(name)
                .setCompleteTypeShow(realType.getTypeName())
                .setReturnTypeShow(realType.getSimpleName())
                .setIfCollection(realType.ifArrayOrCollection())
                .setBelongsToClassName(realType.getCompleteClassName());
        /*  相当于执行了这两个操作 .setDefaultValue("")  .setDescription("") */
        new JavaFieldsVisitor().visit(parentClazz, def);

        if (!realType.ifFinalType()) {
            /* class没有泛型,所以getFieldsTypeShowDefinition 的参数为空map */
            def.setFields(WrapperUtils.getInstance(realType).getFieldsTypeShowDefinition());
        }
        return def;
    }

    @Override
    public boolean ifArray() {
        return true;
    }

    @Override
    public boolean ifArrayOrCollection() {
        return this.ifArray();
    }


    @Override
    public String getSimpleName() {
        String simple = WrapperUtils.getInstance(this.type.getGenericComponentType()).getSimpleName();
        return simple + "[]";
    }


    @Override
    public String getTypeName() {
        return this.type.getTypeName();
    }

    @Override
    public String getDefaultValue() {
        return null;
    }

    @Override
    public boolean ifFinalType() {
        return WrapperUtils.getInstance(this.type.getGenericComponentType()).ifFinalType();
    }
}
