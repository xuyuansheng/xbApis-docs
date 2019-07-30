package cn.xuxiaobu.doc.apis.filter.java;

import cn.xuxiaobu.doc.apis.filter.ApiFilter;

import java.lang.reflect.Method;

/**
 * 接口过滤器,通过过滤器获得实际的接口类和对应方法
 *
 * @author 020102
 * @date 2019-07-18 16:09
 */
public interface JavaApiFilter<T> extends ApiFilter {
    /**
     * 过滤器方法
     * @param target  被过滤的目标
     * @return 结果
     */
    default Boolean doFilter(T target) {
        return true;
    }
}
