package cn.xuxiaobu.doc.apis.definition;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

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
  * 获取目标类
  * @return
  */
 String getCompleteClassName();

    /**
     * 获取所有field的类型
     *
     * @return
     */
    Map<String, TypeWrapper> getFieldsType();

   /**
    * 获取类型的字段信息
    * @param genericitys 如果字段中有泛型时会用上的参数
    * @return  类型内部字段的信息定义
    */
    List<TypeShowDefinition> getFieldsTypeShowDefinition(Map<String, Type> genericitys);

   /**
    * 获取字段的本身的信息
    * @param name  字段名
    * @param parentClazz  字段所属的java类源码解析树
    * @param genericitys  泛型参数,如果字段本身是 GenericArrayType,TypeVariable 可能会替换为实际的类型
    * @return 字段本身的信息定义
    */
    TypeShowDefinition getFieldTypeShowDefinition(String name, ClassOrInterfaceDeclaration parentClazz, Map<String, Type> genericitys);
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



}
