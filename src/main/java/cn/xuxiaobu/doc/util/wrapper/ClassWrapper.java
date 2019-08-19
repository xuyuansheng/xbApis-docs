package cn.xuxiaobu.doc.util.wrapper;

import cn.xuxiaobu.doc.apis.definition.TypeShowDefinition;
import cn.xuxiaobu.doc.apis.definition.TypeWrapper;
import cn.xuxiaobu.doc.apis.enums.FinalJavaType;
import cn.xuxiaobu.doc.apis.processor.note.JavaFieldsVisitor;
import cn.xuxiaobu.doc.util.processor.GenericityUtils;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
    public String getCompleteClassName() {
        return this.type.getTypeName();
    }

    @Override
    public Map<String, TypeWrapper> getFieldsType() {
        Map<String, TypeWrapper> map;
        if (FinalJavaType.exists(this.type)) {
            return new HashMap<>(0);
        }
        if (ifArray()) {
            Class<?> arrayComponentType = type.getComponentType();
            map = Stream.of(arrayComponentType.getDeclaredFields()).filter(field -> !field.getType().equals(arrayComponentType)).collect(Collectors.toMap(Field::getName, WrapperUtils::getInstance));
        } else {
            map = Stream.of(type.getDeclaredFields()).filter(field -> !field.getType().equals(type)).collect(Collectors.toMap(Field::getName, WrapperUtils::getInstance));
        }
        return map;
    }

    @Override
    public List<TypeShowDefinition> getFieldsTypeShowDefinition() {

        Class realType = this.type;
        Field[] fields = realType.getDeclaredFields();
        /* 把自己添加到终点类,防止循环解析 */
        FinalJavaType.add(realType);
        List<TypeShowDefinition> fieldsDef = Stream.of(fields).map(field -> {
            Type fGenericType = field.getGenericType();
            ClassOrInterfaceDeclaration clazz = GenericityUtils.getClassOrInterfaceDeclaration(this.type.getName());
            return WrapperUtils.getInstance(fGenericType).getFieldTypeShowDefinition(field.getName(), clazz, new HashMap<>(0));
        }).collect(Collectors.toList());
        TypeWrapper superType = WrapperUtils.getInstance(realType.getGenericSuperclass());
        if(!superType.ifFinalType()){
            /* 获取父类的属性 */
            fieldsDef.addAll(superType.getFieldsTypeShowDefinition());
        }
        FinalJavaType.remove(realType);
        return fieldsDef;
    }

    @Override
    public TypeShowDefinition getFieldTypeShowDefinition(String name, ClassOrInterfaceDeclaration parentClazz, Map<String, Type> genericitys) {

        TypeShowDefinition def = new TypeShowDefinition()
                .setName(name)
                .setCompleteTypeShow(this.getTypeName())
                .setReturnTypeShow(this.getSimpleName())
                .setIfCollection(this.ifArrayOrCollection())
                .setBelongsToClassName(this.getCompleteClassName());
        /*  相当于执行了这两个操作 .setDefaultValue("")  .setDescription("") */
        new JavaFieldsVisitor().visit(parentClazz, def);

        if (!this.ifFinalType()) {
            /* class没有泛型,所以getFieldsTypeShowDefinition 的参数为空map */
            def.setFields(WrapperUtils.getInstance(this.type).getFieldsTypeShowDefinition());
        }
        return def;
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
