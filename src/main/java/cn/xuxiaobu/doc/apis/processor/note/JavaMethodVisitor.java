package cn.xuxiaobu.doc.apis.processor.note;

import cn.xuxiaobu.doc.apis.definition.DefaultJavaApiDefinition;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
public class JavaMethodVisitor extends VoidVisitorAdapter<DefaultJavaApiDefinition> {

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
            if (methodDeclaration.size() == 1) {
                /* 只找到一个,即匹配上 */
                methodDeclaration.get(0).accept(this, arg);
                return;
            } else if (methodDeclaration.size() < 1) {
                /* 一个都没找到,方法出错 */
                throw new RuntimeException("方法匹配出错");
            }
            /* 到此,找到了多个方法,且参数个数相同,只能一个个去比较参数的类型是否相同 */
            List<MethodDeclaration> methodDeclaration3 = methodDeclaration.stream().filter(me -> {
                NodeList<com.github.javaparser.ast.body.Parameter> params = me.getParameters();
                for (int i = 0; i < params.size(); i++) {
                    String type = params.get(i).getTypeAsString();
                    boolean flag;
                    if (StringUtils.contains(type, ".")) {
                        flag = type.equals(paramsTypeList.get(i).getTypeName());
                    } else {
                        flag = type.equals(paramsTypeList.get(i).getSimpleName());
                    }
                    if (!flag) {
                        return false;
                    }
                }
                return true;
            }).collect(Collectors.toList());

            if (methodDeclaration3.size() == 1) {
                /* 只找到一个,即匹配上 */
                methodDeclaration.get(0).accept(this, arg);
                return;
            } else if (methodDeclaration3.size() < 1) {
                /* 一个都没找到,方法出错 */
                throw new RuntimeException("方法匹配出错");
            }
            /* 找到多个时,则表示带全类名的才是正确方法 */
            Optional<MethodDeclaration> result = methodDeclaration3.stream().filter(mm -> StringUtils.contains(mm.getSignature().asString(), ".")).findFirst();
            if (result.isPresent()) {
                result.get().accept(this, arg);
                return;
            }
        }
        throw new RuntimeException("方法匹配出错");
    }

    @Override
    public void visit(MethodDeclaration n, DefaultJavaApiDefinition arg) {
        SimpleName methodName = n.getName();
        n.getJavadoc().ifPresent(javadoc -> {
            arg.setDescription(javadoc.getDescription().toText());
        });
    }

    /**
     * 获取MethodDeclaration的签名,把其中参数类型为全类名的改为简单类名
     *
     * @param methodDeclaration 方法
     * @return 签名结果
     */
    private String getSignatureAsString(MethodDeclaration methodDeclaration) {
        final String dian = ".";
        String name = methodDeclaration.getSignature().asString();
        if (StringUtils.contains(name, dian)) {
            List<String> params = methodDeclaration.getParameters().stream().map(parameter -> parameter.getTypeAsString()).filter(parameter -> StringUtils.contains(parameter, dian)).collect(Collectors.toList());
            for (String param : params) {
                List<String> list = Stream.of(StringUtils.split(param, dian)).collect(Collectors.toList());
                name = name.replace(param, list.get(list.size() - 1));
            }
        }
        return name;
    }

    /**
     * 获取方法的格式化,如:method1(String,Integer)或者method1(java.lang.String,Integer)
     *
     * @param method       方法
     * @param ifSimpleName 参数类型是否为简单类型 true:简单类名 ,false:全类名
     * @return 格式化结果
     */
    private String printMethodAsString(Method method, boolean ifSimpleName) {
        StringBuilder sb = new StringBuilder();
        String methodName = method.getName();
        sb.append(methodName).append("(");
        boolean firstParam = true;
        Parameter[] params = method.getParameters();
        for (int i = 0; i < params.length; i++) {
            if (firstParam) {
                firstParam = false;
            } else {
                sb.append(", ");
            }
            if (ifSimpleName) {
                sb.append(params[i].getType().getSimpleName());
            } else {
                sb.append(params[i].getType().getTypeName());
            }
        }
        sb.append(")");
        return sb.toString();
    }

}
