package cn.xuxiaobu.doc.util.wrapper;

import cn.xuxiaobu.doc.apis.definition.TypeWrapper;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
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
    public Map<String, TypeWrapper> getFieldsType() {
        return new HashMap<>(0);
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
