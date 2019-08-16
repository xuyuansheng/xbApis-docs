package cn.xuxiaobu.doc.apis.processor.note;

import cn.xuxiaobu.doc.apis.definition.DefaultJavaApiDefinition;
import cn.xuxiaobu.doc.apis.definition.ReturnTypeDefinition;
import cn.xuxiaobu.doc.apis.initialization.JavaSourceFileContext;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.JavadocBlockTag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
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
                List<MethodDeclaration> typeNameMatchedTemp = typeNameMatched.stream().filter(m -> m.getParameter(finalI).getTypeAsString().equals(paramsTypeList.get(finalI).getTypeName())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(typeNameMatchedTemp)) {
                    typeNameMatched = typeNameMatchedTemp;
                    continue;
                }
                /* 上面第 i 个参数的全类名没匹配到,则用简单类名simpleName去匹配,找到了继续下一个参数,没找到表示第 i 个参数没匹配到任何方法,查找失败  */
                typeNameMatched = typeNameMatched.stream().filter(m -> m.getParameter(finalI).getTypeAsString().equals(paramsTypeList.get(finalI).getSimpleName())).collect(Collectors.toList());
            }
            if(typeNameMatched.size()==1){
                /* 只找到一个,即匹配上 */
                typeNameMatched.get(0).accept(this, arg);
                return;
            }else if(typeNameMatched.size()<1){
                /* 一个都没找到,方法出错 */
                log.info("方法匹配出错,类={}\n方法={}\n解析树={}", classData, methodData, n);
            }else {
                /* 一个都没找到,方法出错 */
                log.info("方法匹配到多个,取第一个,类={}\n方法={}\n解析树={}", classData, methodData, n);
                typeNameMatched.get(0).accept(this, arg);
            }
        }
        log.info("方法匹配出错,类={}\n方法={}\n解析树={}", classData, methodData, n);
    }

    @Override
    public void visit(MethodDeclaration n, DefaultJavaApiDefinition arg) {
        Type type = arg.getMethodMateData().getGenericReturnType();
        ReturnTypeDefinition returnTypeDefinition = arg.getReturnTypeDefinition();
        arg.setDescription(getDescription(n, false));
        returnTypeDefinition.init(type, getDescription(n, true));
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

}
