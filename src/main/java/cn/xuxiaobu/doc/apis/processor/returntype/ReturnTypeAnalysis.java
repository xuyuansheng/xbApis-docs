package cn.xuxiaobu.doc.apis.processor.returntype;

import cn.xuxiaobu.doc.apis.definition.ReturnTypeDefinition;
import cn.xuxiaobu.doc.apis.processor.note.TypeWrapper;
import cn.xuxiaobu.doc.util.wrapper.WrapperUtils;
import com.alibaba.fastjson.JSON;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @program: xbapi-docs
 * @description: 返回类型解析
 * @author: Mr.Xu
 * @create: 2019/8/2 20:42
 */
public class ReturnTypeAnalysis<T> {


    private T  t;
    private String  string;
    private List<T>  list;
    private  T[]  ta;


    public ReturnTypeAnalysis<ReturnTypeAnalysis<Integer>> parametertype1(String a) {
        return null;
    }
    public List<ReturnTypeAnalysis>[] parametertype(String a) {
        return null;
    }

    public ReturnTypeAnalysis<Map<String, T>> parametertype() {
        return null;
    }

    public <A> ReturnTypeAnalysis<A> parametertype(int b) {
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



    public static void main(String[] args) throws NoSuchMethodException {

        ReturnTypeDefinition  returnTypeDefinition = new ReturnTypeDefinition();



        ReturnTypeAnalysis returnTypeAnalysis = new ReturnTypeAnalysis();
        Stream.of(ReturnTypeAnalysis.class.getDeclaredMethods()).filter(method -> method.getName().equals("parametertype1")).forEach(method -> {
            Type actualType = method.getGenericReturnType();
            returnTypeDefinition.init(actualType,"heiha");
        });
        String json = JSON.toJSONString(returnTypeDefinition.getReturnType());
        System.out.println(json);
        Stream.of(ReturnTypeAnalysis.class.getDeclaredMethods()).filter(method -> method.getName().equals("parametertype")).forEach(method -> {
            Type actualType = method.getGenericReturnType();
            TypeWrapper act = WrapperUtils.getInstance(actualType);
            System.out.println(act.getSimpleName()+" = "+act.ifArray());

        });


    }





    public boolean ifFinalType(Class<?> clazz) {
        Optional<? extends Class<?>> result = Optional.of(clazz)
                .filter(c -> !c.equals(boolean.class))
                .filter(c -> !c.equals(Boolean.class))
                .filter(c -> !c.equals(Integer.class))
                .filter(c -> !c.equals(boolean.class))
                .filter(c -> !c.equals(boolean.class))
                .filter(c -> !c.equals(boolean.class));
        if (result.isPresent()) {
            return false;
        } else {
            return true;
        }
    }


}
