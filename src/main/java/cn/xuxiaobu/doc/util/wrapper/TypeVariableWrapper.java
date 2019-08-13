package cn.xuxiaobu.doc.util.wrapper;

import cn.xuxiaobu.doc.apis.definition.TypeShowDefinition;
import cn.xuxiaobu.doc.apis.definition.TypeWrapper;
import cn.xuxiaobu.doc.apis.processor.note.JavaFieldsVisitor;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: xbapi-docs
 * @description: TypeVariable类型包装类操作
 * @author: Mr.Xu
 * @create: 2019/8/4 10:10
 */
@Data
@Accessors(chain = true)
public class TypeVariableWrapper implements TypeWrapper {

    private TypeVariable type;

    public TypeVariableWrapper(TypeVariable type) {
        this.type = type;
    }

    @Override
    public String getCompleteClassName() {
        return this.type.getTypeName();
    }

    @Override
    public Map<String, TypeWrapper> getFieldsType() {
        return new HashMap<>(0);
    }

    @Override
    public List<TypeShowDefinition> getFieldsTypeShowDefinition() {
        /* 实际情况进不来此方法,即使进来了也是返回null */
        return null;
    }

    @Override
    public TypeShowDefinition getFieldTypeShowDefinition(String name, ClassOrInterfaceDeclaration parentClazz, Map<String, Type> genericitys) {
        /* 因为是泛型,所以可能有上层传过来的实际类型 */
        String typeName = this.type.getTypeName();
        Type typeReal = genericitys.get(typeName);
        typeReal = typeReal == null ? this.type : typeReal;

        if (typeReal instanceof ParameterizedType) {
            return WrapperUtils.getInstance(typeReal).getFieldTypeShowDefinition(name, parentClazz, genericitys);
        }

        TypeWrapper realType = WrapperUtils.getInstance(typeReal);

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
        return false;
    }

    @Override
    public boolean ifArrayOrCollection() {
        return false;
    }

    @Override
    public String getSimpleName() {
        return this.type.getTypeName();
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
        return true;
    }
}
