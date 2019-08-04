package cn.xuxiaobu.doc.util.wrapper;

import cn.xuxiaobu.doc.apis.enums.FinalJavaType;
import cn.xuxiaobu.doc.apis.processor.note.TypeWrapper;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: xbapi-docs
 * @description: java类型包装类操作
 * @author: Mr.Xu
 * @create: 2019/8/4 10:10
 */
@Data
@Accessors(chain = true)
public class ClassWrapper implements TypeWrapper {

    private Class<?> type;

    public ClassWrapper(Class<?> type) {
        this.type = type;
    }

    @Override
    public Map<String, TypeWrapper> getFieldsType() {
        Map<String, TypeWrapper> map;
        if(FinalJavaType.exists(this.type)){
            return new HashMap<>(0);
        }
        if (ifArray()) {
            Class<?> arrayComponentType = type.getComponentType();
            map = Stream.of(arrayComponentType.getDeclaredFields()).collect(Collectors.toMap(Field::getName, WrapperUtils::getInstance));
        } else {
            map = Stream.of(type.getDeclaredFields()).collect(Collectors.toMap(Field::getName, WrapperUtils::getInstance));
        }
        return map;
    }

    @Override
    public boolean ifArray() {
        return this.type.isArray();
    }

    @Override
    public boolean ifArrayOrCollection() {
        return Collection.class.isAssignableFrom(this.type) || this.ifArray();
    }


    @Override
    public String getSimpleName() {
        return this.type.getSimpleName();
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
        return FinalJavaType.exists(this.type);
    }
}
