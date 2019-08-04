package cn.xuxiaobu.doc.util.wrapper;

import cn.xuxiaobu.doc.apis.processor.note.TypeWrapper;

import java.lang.reflect.Type;
import java.util.HashMap;
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
