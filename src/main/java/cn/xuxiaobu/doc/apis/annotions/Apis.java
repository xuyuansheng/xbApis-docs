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
    /**  */
    String name() default "";
    
    String value() default "";
    
    String[] method()default {};
    
    String[] params() default {};

    String[] headers() default {};

    String[] consumes() default {};

    String[] produces() default {};
    
}
