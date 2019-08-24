package cn.xuxiaobu.doc.util.processor;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.definition.DefaultJavaApiDefinition;
import cn.xuxiaobu.doc.apis.definition.TypeShowDefinition;
import com.alibaba.fastjson.JSON;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 静态页生成工具类
 *
 * @author 020102
 * @date 2019-08-20 09:13
 */
public class ThymeleafUtil {

    public static void main(String[] args) throws IOException {
        String apis = "[{\"definitionFrom\":\"SPRING_JAVA\",\"definitionName\":\"com.study.xu.controller.RestControllerTest : controller\",\"description\":\"hello Controller测试\",\"host\":\"localhost\",\"method\":[\"POST\",\"GET\"],\"port\":\"80\",\"protocol\":\"http\",\"returnTypeDefinition\":{\"description\":\"controller返回值\",\"ifCollection\":false,\"typeShowDefinition\":{\"belongsToClassName\":\"com.study.xu.vo.ResultVo\",\"completeTypeShow\":\"com.study.xu.vo.ResultVo<com.study.xu.domain.TestBean>\",\"defaultValue\":\"\",\"description\":\"controller返回值\",\"fields\":[{\"belongsToClassName\":\"java.lang.String\",\"completeTypeShow\":\"java.lang.String\",\"description\":\"错误码\",\"ifCollection\":false,\"name\":\"code\",\"returnTypeShow\":\"String\"},{\"belongsToClassName\":\"java.lang.String\",\"completeTypeShow\":\"java.lang.String\",\"description\":\"提示信息\",\"ifCollection\":false,\"name\":\"msg\",\"returnTypeShow\":\"String\"},{\"belongsToClassName\":\"boolean\",\"completeTypeShow\":\"boolean\",\"defaultValue\":\"true\",\"description\":\"是否成功 true:成功 , false:失败\",\"ifCollection\":false,\"name\":\"ifSuccess\",\"returnTypeShow\":\"boolean\"},{\"belongsToClassName\":\"com.study.xu.domain.TestBean\",\"completeTypeShow\":\"com.study.xu.domain.TestBean\",\"description\":\"返回数据\",\"fields\":[{\"belongsToClassName\":\"java.lang.String\",\"completeTypeShow\":\"java.lang.String\",\"description\":\"名称\",\"ifCollection\":false,\"name\":\"name\",\"returnTypeShow\":\"String\"},{\"belongsToClassName\":\"java.lang.Integer\",\"completeTypeShow\":\"java.lang.Integer\",\"description\":\"年龄\",\"ifCollection\":false,\"name\":\"age\",\"returnTypeShow\":\"Integer\"},{\"belongsToClassName\":\"java.lang.String\",\"completeTypeShow\":\"java.lang.String\",\"ifCollection\":false,\"name\":\"ori\",\"returnTypeShow\":\"String\"},{\"belongsToClassName\":\"java.util.List\",\"completeTypeShow\":\"java.util.List<java.lang.String>\",\"ifCollection\":true,\"name\":\"dest\",\"returnTypeShow\":\"List<String>\"}],\"ifCollection\":false,\"name\":\"data\",\"returnTypeShow\":\"TestBean\"}],\"ifCollection\":false,\"name\":\"result\",\"returnTypeShow\":\"ResultVo<TestBean>\"}},\"url\":[\"/hello/{param}\"]},{\"definitionFrom\":\"SPRING_JAVA\",\"definitionName\":\"com.study.xu.controller.RestControllerTest : controller\",\"description\":\"测试2222\",\"host\":\"localhost\",\"method\":[\"POST\",\"GET\"],\"port\":\"80\",\"protocol\":\"http\",\"returnTypeDefinition\":{\"description\":\"返回22\",\"ifCollection\":false,\"typeShowDefinition\":{\"belongsToClassName\":\"com.study.xu.genericity.GenericityClass\",\"completeTypeShow\":\"com.study.xu.genericity.GenericityClass<java.util.Map<java.lang.String, java.lang.Object>, com.study.xu.genericity.GenericityTwo<java.util.List, U>, U[], U>\",\"defaultValue\":\"\",\"description\":\"返回22\",\"fields\":[{\"belongsToClassName\":\"java.util.Map\",\"completeTypeShow\":\"java.util.Map<java.lang.String, java.lang.Object>\",\"description\":\"GenericityClass t描述\",\"ifCollection\":false,\"name\":\"t\",\"returnTypeShow\":\"Map<String,Object>\"},{\"belongsToClassName\":\"com.study.xu.genericity.GenericityTwo\",\"completeTypeShow\":\"com.study.xu.genericity.GenericityTwo<java.util.List, U>\",\"description\":\"GenericityClass  arrayR\",\"fields\":[{\"belongsToClassName\":\"java.util.List\",\"completeTypeShow\":\"java.util.List\",\"description\":\"GenericityTwo  t\",\"ifCollection\":true,\"name\":\"t\",\"returnTypeShow\":\"List\"},{\"belongsToClassName\":\"U\",\"completeTypeShow\":\"U\",\"description\":\"GenericityTwo  arrayR\",\"ifCollection\":false,\"name\":\"arrayR\",\"returnTypeShow\":\"U\"},{\"belongsToClassName\":\"java.util.Map\",\"completeTypeShow\":\"java.util.Map<T, R>\",\"description\":\"GenericityTwo  mapTR\",\"ifCollection\":false,\"name\":\"mapTR\",\"returnTypeShow\":\"Map<T,R>\"},{\"belongsToClassName\":\"java.util.List\",\"completeTypeShow\":\"java.util.List<T>\",\"description\":\"GenericityTwo  listT\",\"ifCollection\":true,\"name\":\"listT\",\"returnTypeShow\":\"List<T>\"}],\"ifCollection\":false,\"name\":\"arrayR\",\"returnTypeShow\":\"GenericityTwo<List,U>\"},{\"belongsToClassName\":\"java.util.Map\",\"completeTypeShow\":\"java.util.Map<T, Y>\",\"description\":\"GenericityClass mapTY\",\"ifCollection\":false,\"name\":\"mapTY\",\"returnTypeShow\":\"Map<T,Y>\"},{\"belongsToClassName\":\"java.util.List\",\"completeTypeShow\":\"java.util.List<T>\",\"description\":\"GenericityClass listT\",\"ifCollection\":true,\"name\":\"listT\",\"returnTypeShow\":\"List<T>\"},{\"belongsToClassName\":\"com.study.xu.genericity.GenericityTwo\",\"completeTypeShow\":\"com.study.xu.genericity.GenericityTwo<java.util.Map<R, Y>, U>\",\"description\":\"GenericityClass genericityTwo\",\"fields\":[{\"belongsToClassName\":\"java.util.Map\",\"completeTypeShow\":\"java.util.Map<R, Y>\",\"description\":\"GenericityTwo  t\",\"ifCollection\":false,\"name\":\"t\",\"returnTypeShow\":\"Map<R,Y>\"},{\"belongsToClassName\":\"U\",\"completeTypeShow\":\"U\",\"description\":\"GenericityTwo  arrayR\",\"ifCollection\":false,\"name\":\"arrayR\",\"returnTypeShow\":\"U\"},{\"belongsToClassName\":\"java.util.Map\",\"completeTypeShow\":\"java.util.Map<T, R>\",\"description\":\"GenericityTwo  mapTR\",\"ifCollection\":false,\"name\":\"mapTR\",\"returnTypeShow\":\"Map<T,R>\"},{\"belongsToClassName\":\"java.util.List\",\"completeTypeShow\":\"java.util.List<T>\",\"description\":\"GenericityTwo  listT\",\"ifCollection\":true,\"name\":\"listT\",\"returnTypeShow\":\"List<T>\"}],\"ifCollection\":false,\"name\":\"genericityTwo\",\"returnTypeShow\":\"GenericityTwo<Map<R,Y>,U>\"},{\"belongsToClassName\":\"com.study.xu.genericity.GenericityClass\",\"completeTypeShow\":\"com.study.xu.genericity.GenericityClass<java.lang.String, com.study.xu.genericity.GenericityTwo<java.util.List, U>, U[], U>\",\"description\":\"GenericityClass aa\",\"ifCollection\":false,\"name\":\"aa\",\"returnTypeShow\":\"GenericityClass<String,GenericityTwo<List,U>,U[],U>\"},{\"belongsToClassName\":\"com.study.xu.genericity.GenericityThree\",\"completeTypeShow\":\"com.study.xu.genericity.GenericityThree\",\"description\":\"GenericityClass sup\",\"fields\":[{\"belongsToClassName\":\"T\",\"completeTypeShow\":\"T\",\"description\":\"GenericityThree tSuper\",\"ifCollection\":false,\"name\":\"tSuper\",\"returnTypeShow\":\"T\"},{\"belongsToClassName\":\"R\",\"completeTypeShow\":\"R[]\",\"description\":\"GenericityThree arrayRSuper\",\"ifCollection\":true,\"name\":\"arrayRSuper\",\"returnTypeShow\":\"R[]\"},{\"belongsToClassName\":\"java.util.Map\",\"completeTypeShow\":\"java.util.Map<T, R>\",\"description\":\"GenericityThree mapTRSuper\",\"ifCollection\":false,\"name\":\"mapTRSuper\",\"returnTypeShow\":\"Map<T,R>\"},{\"belongsToClassName\":\"java.util.List\",\"completeTypeShow\":\"java.util.List<T>\",\"description\":\"GenericityThree listTSuper\",\"ifCollection\":true,\"name\":\"listTSuper\",\"returnTypeShow\":\"List<T>\"}],\"ifCollection\":false,\"name\":\"sup\",\"returnTypeShow\":\"GenericityThree\"},{\"belongsToClassName\":\"java.lang.String\",\"completeTypeShow\":\"java.lang.String\",\"description\":\"GenericityThree tSuper\",\"ifCollection\":false,\"name\":\"tSuper\",\"returnTypeShow\":\"String\"},{\"belongsToClassName\":\"java.lang.Integer\",\"completeTypeShow\":\"java.lang.Integer\",\"description\":\"GenericityThree arrayRSuper\",\"ifCollection\":false,\"name\":\"arrayRSuper\",\"returnTypeShow\":\"Integer\"},{\"belongsToClassName\":\"java.util.Map\",\"completeTypeShow\":\"java.util.Map<T, R>\",\"description\":\"GenericityThree mapTRSuper\",\"ifCollection\":false,\"name\":\"mapTRSuper\",\"returnTypeShow\":\"Map<T,R>\"},{\"belongsToClassName\":\"java.util.List\",\"completeTypeShow\":\"java.util.List<T>\",\"description\":\"GenericityThree listTSuper\",\"ifCollection\":true,\"name\":\"listTSuper\",\"returnTypeShow\":\"List<T>\"}],\"ifCollection\":false,\"name\":\"result\",\"returnTypeShow\":\"GenericityClass<Map<String,Object>,GenericityTwo<List,U>,U[],U>\"}},\"url\":[\"/controller2/{param}\"]}]";

        List<ApiDefinition> apiDefinitions = JSON.parseArray(apis, DefaultJavaApiDefinition.class).stream().map(d->(ApiDefinition)d).collect(Collectors.toList());

        ThymeleafUtil.buildHtml("template","D:\\certss\\index.html",apiDefinitions);
    }

    public static void buildHtml(String templateName, String outFile, List<ApiDefinition>  apiDefinitions) throws IOException {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        /* 模板所在目录，相对于当前classloader的classpath。 */
        resolver.setPrefix("templates/");
        /* 模板文件后缀 */
        resolver.setSuffix(".html");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        /* 输出文件 */
        Path filePath = Paths.get(outFile);
        Path parent = filePath.getParent();
        if(parent!=null&&!parent.toFile().exists()){
            parent.toFile().mkdirs();
        }
        Context context = new Context();
        context.setVariable("apiDefinitions", apiDefinitions);
        context.setVariable("typeReturns",new ArrayList<TypeShowDefinition>());
        context.setVariable("lv","");
        context.setVariable("dian",".");
        templateEngine.process(templateName, context, new FileWriter(filePath.toString()));

    }

}
