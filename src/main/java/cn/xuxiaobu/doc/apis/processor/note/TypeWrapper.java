package cn.xuxiaobu.doc.apis.processor.note;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @program: xbapi-docs
 * @description: 类型包装类, 包含了java的类型和对应类型的java源码
 * @author: Mr.Xu
 * @create: 2019/8/4 8:14
 */
public interface TypeWrapper extends Type {
    /**
     * 获取所有field的类型
     *
     * @return
     */
    Map<String, TypeWrapper> getFieldsType();

    /**
     * 是否为数组
     */
    boolean ifArray();

    /**
     * 是否为集合或者数组
     */
    boolean ifArrayOrCollection();

    /**
     * 获取默认值
     */
    String getDefaultValue();

    /**
     * 是否为终点类型,即不会继续解析
     */
    boolean ifFinalType();

    String getSimpleName();

}
