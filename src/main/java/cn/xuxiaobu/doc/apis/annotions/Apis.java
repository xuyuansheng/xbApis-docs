package cn.xuxiaobu.doc.apis.annotions;

import java.lang.annotation.*;

/**
 * 定义API的注解,类似于spring中的Controller或者RequestMapping等注解
 *
 * @author 020102
 * @date 2019-07-29 16:46
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Apis {
    /**
     * 名称
     */
    String name() default "";

    /**
     * url的值
     */
    String value() default "";

    /**
     * 方法支持的请求方式
     */
    String[] method() default {};

    /**
     * 方法参数
     */
    Class<?>[] paramsType() default {};

    /**
     * 方法的返回值
     */
    Class<?> returnType() default Object.class;

    String[] params() default {};

    String[] headers() default {};

    String[] consumes() default {};

    String[] produces() default {};


}
