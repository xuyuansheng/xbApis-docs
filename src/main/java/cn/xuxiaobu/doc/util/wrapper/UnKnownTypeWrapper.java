package cn.xuxiaobu.doc.util.wrapper;

import cn.xuxiaobu.doc.apis.definition.TypeShowDefinition;
import cn.xuxiaobu.doc.apis.definition.TypeWrapper;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: xbapi-docs
 * @description: 未知的type类型, 不做解析
 * @author: Mr.Xu
 * @create: 2019/8/4 21:21
 */
public class UnKnownTypeWrapper implements TypeWrapper {

    private Type type;

    public UnKnownTypeWrapper(Type type) {
        this.type = type;
    }

    @Override
    public String getCompleteClassName() {
        return  this.type.getTypeName();
    }

    @Override
    public Map<String, TypeWrapper> getFieldsType() {
        return new HashMap<>(0);
    }

    @Override
    public List<TypeShowDefinition> getFieldsTypeShowDefinition() {
        return null;
    }

    @Override
    public TypeShowDefinition getFieldTypeShowDefinition(String name, ClassOrInterfaceDeclaration parentClazz, Map<String, Type> genericitys) {
        return  new TypeShowDefinition()
                .setName(name)
                .setCompleteTypeShow(type.getTypeName())
                .setReturnTypeShow(type.getTypeName())
                .setIfCollection(false)
                .setBelongsToClassName(type.getTypeName());
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
    public String getDefaultValue() {
        return "";
    }

    @Override
    public boolean ifFinalType() {
        return true;
    }

    @Override
    public String getSimpleName() {
        return this.type.getTypeName();
    }

    @Override
    public String getTypeName() {
        return this.type.getTypeName();
    }
}
