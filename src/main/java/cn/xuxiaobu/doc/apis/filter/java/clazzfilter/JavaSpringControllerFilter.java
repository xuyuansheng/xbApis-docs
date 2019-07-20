package cn.xuxiaobu.doc.apis.filter.java.clazzfilter;


import cn.xuxiaobu.doc.apis.filter.java.JavaApiFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 020102
 * @date 2019-07-18 16:12
 */
public class JavaSpringControllerFilter implements JavaApiFilter {

    ClassLoader loader;

    public JavaSpringControllerFilter(ClassLoader loader) {
        this.loader = loader;
    }

    @Override
    public Boolean doFilter(String className) {
        try {
            Class<?> clazz = loader.loadClass(className);
            return clazz.isAnnotationPresent(RestController.class) || clazz.isAnnotationPresent(Controller.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
