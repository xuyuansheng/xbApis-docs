package cn.xuxiaobu.doc.apis.initialization;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;

/**
 * 源文件初始化
 *
 * @author 020102
 * @date 2019-07-18 10:29
 */
public interface SourceFileInitialization {

    /**
     * 初始化源码文件夹
     *
     * @param root   存放结果集的Map,最后返回
     * @param source 源文件URL
     * @return
     */
    LinkedHashMap<String, Object> initSourceClasses(LinkedHashMap<String, Object> root, URL source) throws IOException;

    /**
     * 初始化源码压缩文件
     *
     * @param root   存放结果集的Map,最后返回
     * @param source 源文件URL
     * @return
     */
    LinkedHashMap<String, Object> initSourceCompressedFile(LinkedHashMap<String, Object> root, URL source) throws IOException;
}
