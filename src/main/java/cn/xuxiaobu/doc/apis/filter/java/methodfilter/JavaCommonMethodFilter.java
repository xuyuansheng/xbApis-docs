package cn.xuxiaobu.doc.apis.filter.java.methodfilter;

import cn.xuxiaobu.doc.apis.filter.java.JavaApiFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

/**
 * @author 020102
 * @date 2019-07-19 14:09
 */
public class JavaCommonMethodFilter implements JavaApiFilter {
    @Override
    public Boolean doFilter(Method method) {
        return method.isAnnotationPresent(RequestMapping.class)
                || method.isAnnotationPresent(PostMapping.class)
                || method.isAnnotationPresent(GetMapping.class);
    }
}
