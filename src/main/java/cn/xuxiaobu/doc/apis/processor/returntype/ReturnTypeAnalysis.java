package cn.xuxiaobu.doc.apis.processor.returntype;

import cn.xuxiaobu.doc.apis.definition.ReturnTypeDefinition;
import cn.xuxiaobu.doc.apis.definition.TypeShowDefinition;
import cn.xuxiaobu.doc.apis.definition.TypeWrapper;
import cn.xuxiaobu.doc.util.wrapper.WrapperUtils;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: xbapi-docs
 * @description: 返回类型解析
 * @author: Mr.Xu
 * @create: 2019/8/2 20:42
 */
public class ReturnTypeAnalysis<T,R> {


    private T  t;
    private String  string="morenzhi";
    private Integer  integers= 22;
    private List<T>  list=new ArrayList<>();
    private  T[]  ta;
    private ReturnTypeAnalysis returnTypeAnalysis;

    public ReturnTypeAnalysis<ReturnTypeAnalysis<Integer,String>,Boolean> parametertype1(String a) {
        return null;
    }
    public List<ReturnTypeAnalysis>[] parametertype(String a) {
        return null;
    }

    public ReturnTypeAnalysis<Map<String, T>,R> parametertype() {
        return null;
    }

    public <A> ReturnTypeAnalysis<A,R> parametertype(int b) {
        return null;
    }

    public <T> T[] parametertype(Integer b) {
        return null;
    }
    public Object[] parametertype(short b) {
        return null;
    }
    public ReturnTypeAnalysis[] parametertype(byte b) {
        return null;
    }

    public Object parametertype(double b) {
        return null;
    }
    public ReturnTypeAnalysis parametertype(float b) {
        return null;
    }

    public <A> A parametertype(A a) {
        return a;
    }


    ReturnTypeAnalysis method1(){return null;}
    ReturnTypeAnalysis<String,Integer> method2(){return null;}
    ReturnTypeAnalysis<T,Integer> method3(){return null;}
    T method4(){return null;}
    T[] method5(){return null;}



    public static void main(String[] args) throws NoSuchMethodException, FileNotFoundException {

        ReturnTypeDefinition  returnTypeDefinition = new ReturnTypeDefinition();


        Type p1type = ReturnTypeAnalysis.class.getMethod("parametertype1",String.class).getGenericReturnType();

        Stream.of(ReturnTypeAnalysis.class.getDeclaredMethods()).filter(method -> method.getName().startsWith("method"))
//        .map(method -> method.getGenericReturnType())
                .forEach(type -> {
                    Type returntype = type.getGenericReturnType();
                    TypeWrapper wrapper = WrapperUtils.getInstance(returntype);
                    getFields(wrapper);
                });

        TypeWrapper wrapper = WrapperUtils.getInstance(p1type);
        wrapper.getFieldsType();

        ParseResult<CompilationUnit> parseResult = new JavaParser().parse(new FileInputStream(new File("D:\\JavaWorkSpace\\xbapi-docs\\src\\main\\java\\cn\\xuxiaobu\\doc\\apis\\processor\\returntype\\ReturnTypeAnalysis.java")));
        CompilationUnit compilationUnit = parseResult.getResult().orElse(new CompilationUnit());
        compilationUnit.getClassByName("ReturnTypeAnalysis").ifPresent(clazz->{
            clazz.getFields().forEach(fieldDeclaration -> {

            });
        });
    }

    private static List<TypeShowDefinition> getFields(TypeWrapper parent) {
        if(parent.ifFinalType()){
            return Collections.EMPTY_LIST;
        }
        /* 获取到所有的字段 */
        List<TypeShowDefinition> list = parent.getFieldsType().entrySet().stream()
                .map(entry -> {
                    TypeWrapper value = entry.getValue();
                    TypeShowDefinition typeShowDefinition = new TypeShowDefinition()
                            .setName(entry.getKey())
                            .setCompleteTypeShow(parent.getTypeName())
                            .setReturnTypeShow(parent.getSimpleName())
                            .setDefaultValue("")
                            .setDescription("")
                            /*  因为是类里面的字段,所以此处肯定不为空,但是有可能为父类的class名称 */
                            .setBelongsToClassName(parent.getTypeName())
                            .setIfCollection(parent.ifArrayOrCollection())
                            .setFields(getFields(value));
                    return typeShowDefinition;
                }).collect(Collectors.toList());

        return list;
    }

}
