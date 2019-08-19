package cn.xuxiaobu.doc.apis.initialization;

import cn.xuxiaobu.doc.exceptions.InitSourceException;
import cn.xuxiaobu.doc.resource.JarInnerFileResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Java文件的初始化
 *
 * @author 020102
 * @date 2019-07-18 10:01
 */
@Slf4j
public class JavaFileInitializationSupport  {
    /**
     * 文件后缀
     */
    final String fileSuffix = ".java";
    /**
     * 压缩的文件格式
     */
    final String compressedFormat = "jar";
    /**
     * 出现重复文件快速失败
     */
    private Boolean ifFailFast = false;

    public JavaFileInitializationSupport() {
    }

    public JavaFileInitializationSupport(Boolean ifFailFast) {
        this.ifFailFast = ifFailFast;
    }

    /**
     * 初始化源码文件夹
     *
     * @param root   存放结果集的Map,最后返回
     * @param source 源文件URL
     * @return
     */
    public LinkedHashMap<String, Object> initSourceClasses(LinkedHashMap<String, Object> root, URL source) throws MalformedURLException {
        root = Optional.ofNullable(root).orElse(new LinkedHashMap<>());
        File classesSource = new File(source.getFile());
        if (!classesSource.exists() || !classesSource.isDirectory()) {
            return root;
        }
        File[] childFile = classesSource.listFiles();
        for (File f : childFile) {
            LinkedHashMap<String, Object> temp = root;
            if (f.isDirectory()) {
                LinkedHashMap<String, Object> value = new LinkedHashMap<>();
                temp.put(f.getName(), value);
                initSourceClasses(value, f.toURI().toURL());
            } else if (f.isFile() && StringUtils.endsWith(f.getName(), fileSuffix)) {
                String key = StringUtils.replacePattern(f.getName(), fileSuffix, "");
                if (temp.get(key) == null) {
                    temp.put(key, new FileSystemResource(f));
                } else {
                    if (ifFailFast) {
                        log.info("已经存在相同的文件={} , {}",f.getName(),key);
                        throw new InitSourceException(f.getName(),"已经存在相同的文件");
                    }
                }
            } else {

            }
        }
        return root;
    }

    /**
     * 初始化源码压缩文件
     *
     * @param root   存放结果集的Map,最后返回
     * @param source 源文件URL
     * @return
     */
    public LinkedHashMap<String, Object> initSourceCompressedFile(LinkedHashMap<String, Object> root, URL source) throws IOException {
        root = Optional.ofNullable(root).orElse(new LinkedHashMap<>());
        URL jarSource = Optional.ofNullable(source).filter(k -> "file".equals(k.getProtocol()))
                .filter(k -> StringUtils.endsWith(k.getPath(), compressedFormat)).orElse(null);
        if (jarSource == null) {
            return root;
        }
        JarFile jarFile = new JarFile(new File(jarSource.getFile()));
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry next = entries.nextElement();
            String name = next.getName();
            Boolean isDir = StringUtils.endsWith(name, "/");
            LinkedHashMap<String, Object> temp = root;
            String[] array = StringUtils.split(name, "/");
            int length = isDir ? array.length : array.length - 1;
            for (int i = 0; i < length; i++) {
                Object value;
                if ((value = temp.get(array[i])) == null) {
                    value = new LinkedHashMap<>();
                    temp.put(array[i], value);
                }
                temp = (LinkedHashMap) value;
            }
            if (!isDir && StringUtils.endsWithAny(name, fileSuffix)) {
                String key = StringUtils.replacePattern(array[length], fileSuffix, "");
                if (temp.get(key) == null) {
                    temp.put(key, new JarInnerFileResource(jarFile, next));
                } else {
                    if (ifFailFast) {
                        log.info("已经存在相同的文件={} , {}",source,key);
                        throw new InitSourceException(source.getPath(),"已经存在相同的文件");
                    }
                }

            }
        }
        return root;
    }
}
