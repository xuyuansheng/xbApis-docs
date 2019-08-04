package cn.xuxiaobu.doc.util.wrapper;

import cn.xuxiaobu.doc.apis.processor.note.TypeWrapper;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.GenericArrayType;
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
    public Map<String, TypeWrapper> getFieldsType() {
        return WrapperUtils.getInstance(this.type.getGenericComponentType()).getFieldsType();
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
