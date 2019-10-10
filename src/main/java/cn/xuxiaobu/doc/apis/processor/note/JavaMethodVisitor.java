package cn.xuxiaobu.doc.apis.processor.note;

import cn.xuxiaobu.doc.apis.annotions.Apis;
import cn.xuxiaobu.doc.apis.definition.*;
import cn.xuxiaobu.doc.apis.enums.CustomTagType;
import cn.xuxiaobu.doc.apis.initialization.JavaSourceFileContext;
import cn.xuxiaobu.doc.util.processor.AnnotationUtils;
import cn.xuxiaobu.doc.util.regx.PatternInit;
import cn.xuxiaobu.doc.util.wrapper.WrapperUtils;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescription;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 方法的访问器类,用来访问JavaParser解析后的CompilationUnit,从中获取方法的定义,注释等数据
 *
 * @author 020102
 * @date 2019-07-31 09:47
 */
@Slf4j
public class JavaMethodVisitor extends VoidVisitorAdapter<DefaultJavaApiDefinition> {


    private JavaSourceFileContext sourceFileContext;

    public JavaMethodVisitor(JavaSourceFileContext sourceFileContext) {
        this.sourceFileContext = sourceFileContext;
    }

    @Override
    public void visit(CompilationUnit n, DefaultJavaApiDefinition arg) {
        Class<?> classData = arg.getClazzMateData();
        Method methodData = arg.getMethodMateData();
        String methodName = methodData.getName();
        Integer methodCount = methodData.getParameterCount();
        List<? extends Class<?>> paramsTypeList = Stream.of(methodData.getParameters()).map(parameter -> parameter.getType()).collect(Collectors.toList());
        /* 通过该类名获取到对应的类 */
        Optional<ClassOrInterfaceDeclaration> optionalCn = n.getClassByName(classData.getSimpleName());
        if (optionalCn.isPresent()) {
            /* 通过方法的名称和参数的个数进行初步筛选 */
            List<MethodDeclaration> methodDeclaration = optionalCn.get().getMethods().stream()
                    .filter(mdg -> methodName.equals(mdg.getNameAsString()))
                    .filter(mdg -> methodCount.equals(mdg.getParameters().size()))
                    .collect(Collectors.toList());
            List<MethodDeclaration> typeNameMatched = methodDeclaration;
            for (int i = 0; i < paramsTypeList.size(); i++) {
                int finalI = i;
                /* 匹配第 i 个参数的全类名,找到了表示成功,继续下一个 */
                List<MethodDeclaration> typeNameMatchedTemp = typeNameMatched.stream().filter(m -> {
                    String simpleName = paramsTypeList.get(finalI).getTypeName();
                    String typeNmae = m.getParameter(finalI).getTypeAsString();
                    String typeNmaeNoGeneric = StringUtils.replacePattern(typeNmae, PatternInit.classTypeGeneric.toString(), "");
                    return simpleName.equals(typeNmaeNoGeneric);
                }).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(typeNameMatchedTemp)) {
                    typeNameMatched = typeNameMatchedTemp;
                    continue;
                }
                /* 上面第 i 个参数的全类名没匹配到,则用简单类名simpleName去匹配,找到了继续下一个参数,没找到表示第 i 个参数没匹配到任何方法,查找失败  */
                typeNameMatched = typeNameMatched.stream().filter(m -> {
                    String simpleName = paramsTypeList.get(finalI).getSimpleName();
                    String typeNmae = m.getParameter(finalI).getTypeAsString();
                    String typeNmaeNoGeneric = StringUtils.replacePattern(typeNmae, PatternInit.classTypeGeneric.toString(), "");
                    return simpleName.equals(typeNmaeNoGeneric);
                }).collect(Collectors.toList());
            }
            if (typeNameMatched.size() == 1) {
                /* 只找到一个,即匹配上 */
                typeNameMatched.get(0).accept(this, arg);
                return;
            } else if (typeNameMatched.size() < 1) {
                /* 一个都没找到,方法出错 */
                log.info("方法匹配出错,类={}\n方法={}\n解析树={}", classData, methodData, n);
            } else {
                /* 一个都没找到,方法出错 */
                log.info("方法匹配到多个,取第一个,类={}\n方法={}\n解析树={}", classData, methodData, n);
                typeNameMatched.get(0).accept(this, arg);
            }
        }
        log.info("方法匹配出错,类={}\n方法={}\n解析树={}", classData, methodData, n);
    }

    @Override
    public void visit(MethodDeclaration n, DefaultJavaApiDefinition arg) {
        Type type = getMethodReturnType(arg.getMethodMateData());
        ReturnTypeDefinition returnTypeDefinition = arg.getReturnTypeDefinition();
        arg.setDescription(getDescription(n, false));
        returnTypeDefinition.init(type, getDescription(n, true));
        /* 解析方法参数 */
        arg.setParam(getParams(n, arg.getMethodMateData()));
    }

    /**
     * 获取方法的描述,而不是方法返回的描述,如:(@return 方法返回值的描述)
     *
     * @param n        方法的源码解析对象
     * @param ifReturn true:获取方法返回的注释 , false:获取方法的注释
     * @return
     */
    private String getDescription(MethodDeclaration n, boolean ifReturn) {
        JavadocBlockTag.Type type = JavadocBlockTag.Type.RETURN;
        StringBuilder result = new StringBuilder();
        n.getJavadoc().ifPresent(javadoc -> {
            if (ifReturn) {
                /* 方法返回值描述 @return 方法返回值的描述 */
                Optional<JavadocBlockTag> returnBlock = javadoc.getBlockTags().stream().filter(javadocBlockTag -> javadocBlockTag.getType().equals(type)).findFirst();
                returnBlock.ifPresent(block -> {
                    result.append(block.getContent().toText());
                });
            } else {
                String methodDescription = javadoc.getDescription().toText();
                result.append(methodDescription);
            }
        });
        return result.toString();
    }

    /**
     * 获取方法参数的数据
     *
     * @param n      方法的解析树
     * @param method 方法
     * @return 列表
     */
    private List<TypeShowDefinition> getParams(MethodDeclaration n, Method method) {
        Parameter[] params = method.getParameters();
        Apis paramsApis = AnnotationUtils.getApisAnnotation(method);
        JavadocBlockTag.Type type = JavadocBlockTag.Type.PARAM;
        Map<String, JavadocBlockTag> paramsDoc = n.getJavadoc().orElse(new Javadoc(new JavadocDescription())).getBlockTags().stream()
                .filter(p -> p.getType().equals(type)).collect(Collectors.toMap(k -> k.getName().orElse(RandomStringUtils.random(10)), v -> v, (m1, m2) -> m2));
        List<TypeShowDefinition> typeShows = Stream.iterate(0, i -> i + 1).limit(params.length).map(i -> {
            Parameter parameter = params[i];
            JavadocBlockTag paramDoc = paramsDoc.get(parameter.getName());
            if (paramDoc == null) {
                return null;
            }
            Class<?> parameterType = parameter.getType();
            if (paramsApis != null && paramsApis.paramsType().length > i) {
                /* 注解不为null 且 注解中的paramsType的长度大于 i */
                parameterType = paramsApis.paramsType()[i];
            }

            List<CustomTags> defaultValueList = CustomTagType.defaultValue.getTagFromString(paramDoc.getContent().toText()).orElse(null);
            String defaultValue = defaultValueList == null ? "" : defaultValueList.get(0).getValue();
            TypeWrapper parameterTypeWrapper = WrapperUtils.getInstance(parameterType);
            TypeShowDefinition typeShowDefinition = new TypeShowDefinition()
                    .setName(parameter.getName())
                    .setCompleteTypeShow(parameterTypeWrapper.getTypeName())
                    .setReturnTypeShow(parameterTypeWrapper.getSimpleName())
                    .setDefaultValue(defaultValue)
                    .setDescription(paramDoc.getContent().toText())
                    .setIfCollection(parameterTypeWrapper.ifArrayOrCollection())
                    .setBelongsToClassName(parameterTypeWrapper.getCompleteClassName());

            if (!parameterTypeWrapper.ifFinalType()) {
                typeShowDefinition.setFields(parameterTypeWrapper.getFieldsTypeShowDefinition());
            }
            return typeShowDefinition;
        }).filter(f -> f != null).collect(Collectors.toList());
        return typeShows;
    }

    /**
     * 获取方法的返回值
     *
     * @param method 方法
     * @return
     */
    private Type getMethodReturnType(Method method) {
        final String voidString = "void";
        Type type = method.getGenericReturnType();
        if (voidString.equals(type.getTypeName())) {
            Apis apis = AnnotationUtils.getApisAnnotation(method);
            if (apis == null || Object.class.equals(apis.returnType())) {
                /* 没找到注解或者注解的returnType只是默认值 */
                return type;
            }
            return apis.returnType();
        }
        return type;
    }

}
