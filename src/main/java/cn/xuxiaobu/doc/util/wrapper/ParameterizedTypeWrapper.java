package cn.xuxiaobu.doc.util.wrapper;

import cn.xuxiaobu.doc.apis.definition.TypeShowDefinition;
import cn.xuxiaobu.doc.apis.definition.TypeWrapper;
import cn.xuxiaobu.doc.apis.processor.note.JavaFieldsVisitor;
import cn.xuxiaobu.doc.util.processor.GenericityUtils;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: xbapi-docs
 * @description: ParameterizedType包装类
 * @author: Mr.Xu
 * @create: 2019/8/4 10:22
 */
public class ParameterizedTypeWrapper implements TypeWrapper {


    private ParameterizedType type;

    public ParameterizedTypeWrapper(ParameterizedType type) {
        this.type = type;

    }

    @Override
    public String getCompleteClassName() {
        return  this.type.getRawType().getTypeName();
    }

    @Override
    public Map<String, TypeWrapper> getFieldsType() {
        Map<String, TypeWrapper> result = WrapperUtils.getInstance(this.type.getRawType()).getFieldsType();

        result.values().forEach(fields->{
            if(fields instanceof ParameterizedTypeWrapper){

            }else  if(fields instanceof GenericArrayTypeWrapper){

            }else  if(fields instanceof TypeVariableWrapper){

            }else {

            }
        });

//        Stream.of(this.type.getActualTypeArguments()).map(WrapperUtils::getInstance).map(typeWrapper -> {
//            Map<String, TypeWrapper> fields = typeWrapper.getFieldsType();
//        })

        return result;
    }

    @Override
    public List<TypeShowDefinition> getFieldsTypeShowDefinition() {
        ParameterizedType realType = this.type;
        Class<?> rawType = Class.class.cast(realType.getRawType());
        Field[] fields = rawType.getDeclaredFields();
        ClassOrInterfaceDeclaration parentClazzUnit = GenericityUtils.getClassOrInterfaceDeclaration(rawType.getTypeName());
        List<TypeShowDefinition> fieldsDef = Stream.of(fields).map(field -> {
            Type fGenericType = field.getGenericType();
            Map<String, Type> map = GenericityUtils.getMethodTypeGenericity(parentClazzUnit, realType);
            return WrapperUtils.getInstance(fGenericType).getFieldTypeShowDefinition(field.getName(),parentClazzUnit,map);
        }).collect(Collectors.toList());
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
            def.setFields(this.getFieldsTypeShowDefinition());
        }
        return def;
    }


    @Override
    public boolean ifArray() {
        return WrapperUtils.getInstance(this.type.getRawType()).ifArray();
    }

    @Override
    public boolean ifArrayOrCollection() {
        return WrapperUtils.getInstance(this.type.getRawType()).ifArrayOrCollection();
    }

    @Override
    public String getSimpleName() {
        StringBuilder result = new StringBuilder();
        String rawType = WrapperUtils.getInstance(this.type.getRawType()).getSimpleName();
        result.append(rawType).append("<");
        Stream.of(this.type.getActualTypeArguments()).forEach(ar -> {
            String actualArgument = WrapperUtils.getInstance(ar).getSimpleName();
            result.append(actualArgument).append(",");
        });
        result.append(">");
        return StringUtils.replace(result.toString(), ",>", ">");
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
      return   WrapperUtils.getInstance(this.type.getRawType()).ifFinalType();
    }
}
