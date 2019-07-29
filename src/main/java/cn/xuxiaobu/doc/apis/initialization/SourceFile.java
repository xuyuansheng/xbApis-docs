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

    InputStream getStream(String index) throws IOException;

    Resource getResource(String index);

    void addSources(URL source) throws IOException;

}
