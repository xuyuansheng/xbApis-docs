package cn.xuxiaobu.doc.apis.filter.java;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 过滤器链工具
 *
 * @author 020102
 * @date 2019-07-29 16:17
 */
public class ChainFilterUtils<T> {

    List<JavaApiFilter<T>> chain;

    public ChainFilterUtils(JavaApiFilter<T>... chain) {
        this.chain = Stream.of(chain).collect(Collectors.toList());
    }

    public Boolean doFilterOr(T target) {
        for (JavaApiFilter filter : chain) {
            if (filter.doFilter(target)) {
                return true;
            }
        }
        return false;
    }

    public Boolean doFilterAnd(T target) {
        for (JavaApiFilter filter : chain) {
            if (!filter.doFilter(target)) {
                return false;
            }
        }
        return true;
    }
}
