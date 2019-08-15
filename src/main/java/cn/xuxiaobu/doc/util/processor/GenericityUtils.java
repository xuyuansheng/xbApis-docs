package cn.xuxiaobu.doc.util.processor;

import cn.xuxiaobu.doc.apis.initialization.JavaSourceFileContext;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.springframework.util.CollectionUtils;
import sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 泛型工具类
 *
 * @author 020102
 * @date 2019-08-13 09:39
 */
public class GenericityUtils {

    /**
     * java源码文件环境
     */
    private static JavaSourceFileContext javaSourceFileContext = null;

    public static void setJavaSourceFileContext(JavaSourceFileContext javaSourceFileContext) {
        GenericityUtils.javaSourceFileContext = javaSourceFileContext;
    }

    /**
     * 通过路径获取到类的解析树
     *
     * @param className java文件类名称
     * @return
     */
    public static ClassOrInterfaceDeclaration getClassOrInterfaceDeclaration(String className) {


        try {
            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(javaSourceFileContext.getResource(className).getInputStream());
            CompilationUnit compilationUnit = parseResult.getResult().orElse(new CompilationUnit());
            Optional<ClassOrInterfaceDeclaration> clazzSource = compilationUnit.getClassByName(className);
            return clazzSource.orElse(new ClassOrInterfaceDeclaration());
        } catch (Exception e) {
            e.printStackTrace();
            return new ClassOrInterfaceDeclaration();
        }
    }

    /**
     * 泛型和实际类型对应
     *
     * @param clazz 定义有泛型的java源码类 如:GenericityClass<T,R,Y,U>
     * @param type  类的泛型应用到实际方法或字段时的实际类型 如: GenericityClass<String, GenericityTwo<List, U>, U[], U>
     * @return 两个的对应关系, 如:通过map.get("R)能找到 GenericityTwo<List, U>
     */
    public static Map<String, Type> getTypeParamters(ClassOrInterfaceDeclaration clazz, ParameterizedType type, Map<String, Type> genericity) {
        /* private  GenericityTwo<Map<R,Y>,U>  genericityTwo;
           private  GenericityClass<String, GenericityTwo<List, U>, U[], U>  aa;
        */
        List<Type> listActType = Stream.of(type.getActualTypeArguments()).collect(Collectors.toList());
        if (genericity != null && genericity.size() > 0) {
            listActType.stream().filter(t -> t instanceof TypeVariable).map(t -> t = genericity.get(t.getTypeName())).collect(Collectors.toList());
            listActType.stream().filter(t -> t instanceof GenericArrayType).map(t -> t = GenericArrayTypeImpl.make(genericity.get(t.getTypeName().replace("[]", "")))).collect(Collectors.toList());
            listActType.stream().filter(t -> t instanceof ParameterizedType).map(t -> {
                ParameterizedType real = ParameterizedType.class.cast(t);
                Type[] actTypeArg = real.getActualTypeArguments();
                Stream.iterate(0, i -> i++).limit(actTypeArg.length).forEach(i -> {
                    Type act = actTypeArg[i];
                    if (act instanceof TypeVariable) {
                        actTypeArg[i] = genericity.get(act.getTypeName());
                    } else if (act instanceof GenericArrayType) {
                        actTypeArg[i] = GenericArrayTypeImpl.make(genericity.get(act.getTypeName().replace("[]", "")));
                    }
                });
                return (t = ParameterizedTypeImpl.make(Class.class.cast(real.getRawType()), actTypeArg, real.getOwnerType()));
            }).collect(Collectors.toList());
        }

        /* 自己的泛型定义列表 GenericityClass<T,R,Y,U> */
        List<String> listTypeParam = clazz.getTypeParameters().stream().map(typeParam -> typeParam.getNameAsString()).collect(Collectors.toList());
        if (listActType.size() != listTypeParam.size()) {
            throw new RuntimeException("泛型无法匹配");
        }
        Map<String, Type> selfTypeParameters = Stream.iterate(0, i -> i++).limit(listActType.size()).collect(Collectors.toMap(k -> listTypeParam.get(k), v -> listActType.get(v)));
        return selfTypeParameters;
    }


    /**
     * 获取方法返回的类型中泛型的对应
     *
     * @param clazz 方法所属类的解析树
     * @param type  方法的返回类型 通过 getGenericReturnType 方法获得
     * @return 方法类的泛型和实际类的对应关系
     */
    public static Map<String, Type> getMethodTypeGenericity(ClassOrInterfaceDeclaration clazz, ParameterizedType type) {
        /*  GenericityClass<? extends List<U>, GenericityTwo<List, U>, U[], U>     */
        List<Type> listActType = Stream.of(type.getActualTypeArguments()).collect(Collectors.toList());
        /* 自己的泛型定义列表 GenericityClass<T,R,Y,U> */
        List<String> listTypeParam = clazz.getTypeParameters().stream().map(typeParam -> typeParam.getNameAsString()).collect(Collectors.toList());
        if (listActType.size() != listTypeParam.size()) {
            throw new RuntimeException("泛型无法匹配");
        }
        Map<String, Type> selfTypeParameters = Stream.iterate(0, i -> i++).limit(listActType.size()).collect(Collectors.toMap(k -> listTypeParam.get(k), v -> listActType.get(v)));
        return selfTypeParameters;
    }


    /**
     * @param parameterizedType 目标类型
     * @param genericty         目标类型所属类定义的泛型的实际类型 getMethodTypeGenericity
     * @return
     */
    public static ParameterizedType genericityReplace(ParameterizedType parameterizedType, Map<String, Type> genericty) {
        if (CollectionUtils.isEmpty(genericty)) {
            return parameterizedType;
        }
        Type[] actTypeArgs = parameterizedType.getActualTypeArguments();
        for (int i = 0; i < actTypeArgs.length; i++) {
            Type actType = actTypeArgs[i];
            if (actType instanceof TypeVariable) {
                actTypeArgs[i] = genericty.get(actType.getTypeName());
            } else if (actType instanceof GenericArrayType) {
                actTypeArgs[i] = GenericArrayTypeImpl.make(genericty.get(actType.getTypeName().replace("[]", "")));
            } else if (actType instanceof WildcardType) {
//                WildcardTypeImpl.make()
            } else if (actType instanceof ParameterizedType) {
                ParameterizedType realType = ParameterizedType.class.cast(actType);
                actTypeArgs[i] = genericityReplace(realType, genericty);
            }
        }
        return ParameterizedTypeImpl.make(Class.class.cast(parameterizedType.getRawType()), actTypeArgs, parameterizedType.getOwnerType());
    }

}
