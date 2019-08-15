package cn.xuxiaobu.doc.apis.definition;


import cn.xuxiaobu.doc.apis.initialization.JavaSourceFileContext;
import cn.xuxiaobu.doc.util.processor.GenericityUtils;
import cn.xuxiaobu.doc.util.wrapper.WrapperUtils;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 返回类型的显示结构定义
 *
 * @author 020102
 * @date 2019-08-01 13:54
 */
@Data
@Accessors(chain = true)
@Slf4j
public class ReturnTypeDefinition {


    public ReturnTypeDefinition() {
    }


    public void init(Type returnType, String description, JavaSourceFileContext sourceFileContext) {
        if (returnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(returnType);
            Class rawType = Class.class.cast(parameterizedType.getRawType());
            String index = rawType.getName();
            String name = rawType.getSimpleName();

            ClassOrInterfaceDeclaration clazzUnit = GenericityUtils.getClassOrInterfaceDeclaration(index);
            /* 获取到泛型和对应实际类型的对应关系 */
            Map<String, Type> genericity = GenericityUtils.getMethodTypeGenericity(clazzUnit, parameterizedType);
            /* 把泛型替换为实际的类型(虽然里面也有可能包含泛型) */
            returnType = GenericityUtils.genericityReplace(parameterizedType, genericity);

        }
        TypeWrapper realType = WrapperUtils.getInstance(returnType);
        this.returnType = realType;
        this.description = description;
        this.ifCollection = realType.ifArray();
        this.typeShowDefinition = getTypeShowDefinition();
    }


    /**
     * 实际的放回类型
     */
    private TypeWrapper returnType;
    /**
     * 返回结果的描述(注意:不是返回类型中各个字段含义的描述,而是对整个返回结果的描述)
     */
    private String description;

    /**
     * 是否为集合或数组
     */
    private boolean ifCollection;
    /**
     * 显示的信息
     */
    private TypeShowDefinition typeShowDefinition;

    /**
     * 获取method本身的注释
     *
     * @return
     */
    private TypeShowDefinition getTypeShowDefinition() {
        TypeShowDefinition typeShowDefinition = new TypeShowDefinition()
                .setName("result")
                .setCompleteTypeShow(returnType.getTypeName())
                .setReturnTypeShow(returnType.getSimpleName())
                .setDefaultValue("")
                .setDescription(this.description)
                .setIfCollection(returnType.ifArrayOrCollection())
                .setBelongsToClassName(returnType.getCompleteClassName());
        if (!this.returnType.ifFinalType()) {
            typeShowDefinition.setFields(returnType.getFieldsTypeShowDefinition());
        }
        return typeShowDefinition;
    }

    /**
     * @param type         NotNull  字段或方法返回值类型
     * @param name         NotNull  字段名称(方法返回值写result)
     * @param parentSource NotNull  方法或字段所属类的源码路径(用于解析描述或默认值等)
     * @param genericitys  NotNull  方法或字段类型包含泛型时的泛型对应关系
     * @return
     */
//    TypeShowDefinition getFieldObject(Type type, String name, Path parentSource, Map<String, Type> genericitys) {
//        return getFieldObject(type, name, GenericityUtils.getClassOrInterfaceDeclaration(parentSource), genericitys);
//    }

//    TypeShowDefinition getFieldObject(Type type, String name, ClassOrInterfaceDeclaration parentClazz, Map<String, Type> genericitys) {
//        if (type instanceof TypeVariable || type instanceof GenericArrayType) {
//            String typeName = type.getTypeName().replace("[]", "");
//            Type typeP = genericitys.get(typeName);
//            type = typeP == null ? type : typeP;
//        }
//        if (type instanceof ParameterizedType) {
//            Path selfSourcePath = Paths.get(type.getClass().getName());
//            ParameterizedType realType = ParameterizedType.class.cast(type);
//            ClassOrInterfaceDeclaration cid = GenericityUtils.getClassOrInterfaceDeclaration(selfSourcePath);
//            genericitys = GenericityUtils.getTypeParamters(cid, realType,genericitys);
//        }
//
//        TypeShowDefinition def = new TypeShowDefinition()
//                .setName(name)
//                .setCompleteTypeShow(returnType.getTypeName())
//                .setReturnTypeShow(returnType.getSimpleName())
//                .setDefaultValue("")
//                .setDescription("")
//                .setIfCollection(returnType.ifArrayOrCollection())
//                .setBelongsToClassName("");
//
//
//        if (!WrapperUtils.getInstance(type).ifFinalType()) {
//            def.setFields(getFields(type, genericitys));
//        }
//        return def;
//    }


    /**
     * @param parent      要解析的类型(即,所属类的类型)
     * @param genericitys 所解析的类型有定义泛型,则为实际的泛型对应
     * @return
     */
//    List<TypeShowDefinition> getFields( Type parent, Map<String, Type> genericitys) {
//
//        if (parent instanceof ParameterizedType) {
//            ParameterizedType realType = ParameterizedType.class.cast(parent);
//            Class<?> rawType = Class.class.cast(realType.getRawType());
//            Field[] fields = rawType.getDeclaredFields();
//            List<TypeShowDefinition> fieldsDef = Stream.of(fields).map(field -> {
//                Type fGenericType = field.getGenericType();
//                return getFieldObject(fGenericType, field.getName(), Paths.get(""), genericitys);
//            }).collect(Collectors.toList());
//           return  fieldsDef;
//        } else if (parent instanceof Class) {
//            Class realType = Class.class.cast(parent);
//            Field[] fields = realType.getDeclaredFields();
//            List<TypeShowDefinition> fieldsDef = Stream.of(fields).map(field -> {
//                Type fGenericType = field.getGenericType();
//                return getFieldObject(fGenericType, field.getName(), Paths.get(""), genericitys);
//            }).collect(Collectors.toList());
//
//            return  fieldsDef;
//        } else if (parent instanceof TypeVariable || parent instanceof GenericArrayType) {
//            /* 这两种类型不可能有字段,所以肯定进不来 */
//        } else {
//
//        }
//        return null;
//    }


}
