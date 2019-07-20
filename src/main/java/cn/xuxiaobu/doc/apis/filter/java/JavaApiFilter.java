package cn.xuxiaobu.doc.apis.filter.java;

import cn.xuxiaobu.doc.apis.filter.ApiFilter;

import java.lang.reflect.Method;

/**
 * 接口过滤器,通过过滤器获得实际的接口类和对应方法
 *
 * @author 020102
 * @date 2019-07-18 16:09
 */
public interface JavaApiFilter extends ApiFilter {
    default Boolean doFilter(String className) {
        return true;
    }

    default Boolean doFilter(Method method) {
        return true;
    }
}
