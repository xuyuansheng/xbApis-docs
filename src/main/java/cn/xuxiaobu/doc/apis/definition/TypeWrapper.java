package cn.xuxiaobu.doc.apis.definition;

import java.lang.reflect.Type;
import java.util.List;
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
     *
     * @return true:是数组
     */
    boolean ifArray();

    /**
     * 是否为集合或者数组
     *
     * @return true:是数据或者集合
     */
    boolean ifArrayOrCollection();

    /**
     * 获取默认值
     *
     * @return 类型的默认值
     */
    String getDefaultValue();

    /**
     * 是否为终点类型,即不会继续解析
     *
     * @return true:是终点type
     */
    boolean ifFinalType();

    /**
     * 获取简单类型名称
     *
     * @return 简单名称
     */
    String getSimpleName();

    /**
     * 接收访问器,对其进行解析,得到想要的数据格式
     * @param analysis
     * @return
     */
    default List<TypeDefinition> accept(TypeAnalysis analysis) {
       return analysis.visit(this);
    }

}
