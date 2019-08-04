package cn.xuxiaobu.doc.util.wrapper;

import cn.xuxiaobu.doc.apis.processor.note.TypeWrapper;

import java.lang.reflect.*;

/**
 * @program: xbapi-docs
 * @description: 包装类的工具类
 * @author: Mr.Xu
 * @create: 2019/8/4 10:35
 */
public class WrapperUtils {

    /**
     * 获取type的包装类型,根据实际类型去包装
     * @param type  实际类型
     * @return
     */
   public static TypeWrapper getInstance(Type type) {
       if (type instanceof ParameterizedType) {
           ParameterizedType actualType = ParameterizedType.class.cast(type);
           return new ParameterizedTypeWrapper(actualType);
       } else if (type instanceof TypeVariable) {
           TypeVariable actualType = TypeVariable.class.cast(type);
           return new TypeVariableWrapper(actualType);
       } else if (type instanceof GenericArrayType) {
           GenericArrayType actualType = GenericArrayType.class.cast(type);
           return new GenericArrayTypeWrapper(actualType);
       } else if (type instanceof Class) {
           Class actualType = Class.class.cast(type);
           return new ClassWrapper(actualType);
       } else {
           throw new RuntimeException("不是指定类型");
       }
   }

    /**
     * 获取字段的类型
     * @param field  字段
     * @return
     */
    public static TypeWrapper getInstance(Field field) {
        return getInstance(field.getGenericType());
    }


}
