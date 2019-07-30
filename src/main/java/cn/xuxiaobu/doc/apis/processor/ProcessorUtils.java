package cn.xuxiaobu.doc.apis.processor;

import org.apache.commons.lang3.StringUtils;

/**
 * 处理过程工具类
 *
 * @author 020102
 * @date 2019-07-30 09:37
 */
public class ProcessorUtils {
    /**
     * 给URL前面加上斜线
     * @param url
     * @return
     */
    public static String urlFormat(String url) {
        boolean flag = StringUtils.startsWith(url, "/")||StringUtils.isBlank(url);
        if (flag) {
            return url;
        } else {
            return "/" + url;
        }
    }

}
