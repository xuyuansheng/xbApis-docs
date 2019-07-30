package cn.xuxiaobu.doc.apis.initialization;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 源文件
 *
 * @author 020102
 * @date 2019-07-18 09:59
 */
public interface SourceFile {
    /**
     * 获取数据流
     * @param index 类的全路径名
     * @return
     * @throws IOException
     */
    InputStream getStream(String index) throws IOException;

    /**
     * 获取源文件
     * @param index 类的全路径名
     * @return
     */
    Resource getResource(String index);

    /**
     * 添加源文件
     * @param source
     * @throws IOException
     */
    void addSources(URL source) throws IOException;

}
