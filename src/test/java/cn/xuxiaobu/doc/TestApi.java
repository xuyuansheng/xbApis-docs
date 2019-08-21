package cn.xuxiaobu.doc;

import cn.xuxiaobu.doc.apis.annotions.Apis;
import cn.xuxiaobu.doc.apis.annotions.JdkDynamicProxy;
import cn.xuxiaobu.doc.apis.initialization.JavaFileInitializationSupport;
import cn.xuxiaobu.doc.apis.initialization.JavaSourceFileContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 测试
 *
 * @author 020102
 * @date 2019-07-18 13:03
 */
@Slf4j
@RequestMapping()
@Apis(value = "value",returnType = ClassLoader.class)
public class TestApi {

    @Test
    public void test(){
        Apis apis = TestApi.class.getAnnotation(Apis.class);
        boolean is = Object.class.equals(Object.class);
        boolean a = Apis.class.isAssignableFrom(apis.returnType());
        boolean b = new JdkDynamicProxy(apis).getProxy().getClass().equals(Apis.class);
    }

    @Test
    public void testSourceClasses() throws Exception {
        String sourceJar = "D:\\JavaProgramFiles\\Maven\\repository\\org\\projectlombok\\lombok\\1.18.8\\lombok-1.18.8-sources.jar";
        String sourceClasses = "D:\\JavaWorkSpace\\xbapi-docs\\src\\main\\java";
        List<URL> urls = new ArrayList<>();
        urls.add(new File(sourceJar).toURI().toURL());
        urls.add(new File(sourceClasses).toURI().toURL());

        JavaFileInitializationSupport javaFileInitializationSupport = new JavaFileInitializationSupport();
        JavaSourceFileContext javaSourceFileContext = new JavaSourceFileContext(javaFileInitializationSupport, urls);

        javaSourceFileContext.getStream("cn.xys.maven.api.indexinitialization.JavaFileInitializationSupport");
        javaSourceFileContext.getStream("org.springframework.core.io.AbstractFileResolvingResource");

        System.out.println(javaSourceFileContext);
    }

}
