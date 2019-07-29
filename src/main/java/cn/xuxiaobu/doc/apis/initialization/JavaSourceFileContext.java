package cn.xuxiaobu.doc.apis.initialization;

import cn.xuxiaobu.doc.exceptions.InitSourceException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 源码文件上下文
 *
 * @author 020102
 * @date 2019-07-18 10:27
 */
@Getter
public class JavaSourceFileContext implements SourceFile {

    private JavaFileInitializationSupport javaFileInitializationSupport;

    private LinkedHashMap root = new LinkedHashMap(16);

    private List<URL> sourceFile;

    public JavaSourceFileContext(JavaFileInitializationSupport javaFileInitializationSupport) {
        this.javaFileInitializationSupport = javaFileInitializationSupport;
        this.sourceFile = new ArrayList<>(0);
    }

    public JavaSourceFileContext(JavaFileInitializationSupport javaFileInitializationSupport, List<URL> source) {
        this.javaFileInitializationSupport = javaFileInitializationSupport;
        this.sourceFile = source;
        try {
            init();
        } catch (IOException e) {
            throw new InitSourceException(e.getMessage(), e);
        }
    }


    /**
     * 初始化源文件数据
     *
     * @throws IOException
     */
    private void init() throws IOException {
        for (URL url : sourceFile) {
            root = javaFileInitializationSupport.initSourceClasses(root, url);
            root = javaFileInitializationSupport.initSourceCompressedFile(root, url);
        }
    }

    @Override
    public InputStream getStream(String index) throws IOException {
        Resource resource = this.getResource(index);
        return resource==null?null:resource.getInputStream();
    }

    @Override
    public Resource getResource(String index) {
        String[] names = StringUtils.split(index, ".");
        LinkedHashMap temp = root;
        Object value = null;
        for (int i = 0; i < names.length; i++) {
            String key = names[i];
            if ((value = temp.get(key)) == null) {
                break;
            }
            if (value instanceof Resource) {
                return ((Resource) value);
            }
            temp = (LinkedHashMap) value;
        }
        return null;
    }

    /**
     * 添加源文件 源文件url
     *
     * @param source
     * @throws IOException
     */
    @Override
    public void addSources(URL source) throws IOException {
        root = javaFileInitializationSupport.initSourceClasses(root, source);
        root = javaFileInitializationSupport.initSourceCompressedFile(root, source);
        this.sourceFile.add(source);
    }


    /**
     * 获取java文件
     * @return
     */
    public List<String> getJavaFileNames() {
        List<File> list = sourceFile.stream().map(s -> new File(s.getPath()))
                .filter(File::exists)
                .filter(File::isDirectory)
                .collect(Collectors.toList());
        ArrayList<String> names = new ArrayList<>();
        list.forEach(f->doGetJavaFileNames(names,f,f.getPath()));
        return names;
    }

    private void doGetJavaFileNames(List<String> names, File file, String rootPath) {
        final String suffix = ".java";
        final String dot = ".";
        final String infoJava = "package-info";
        names = Optional.ofNullable(names).orElse(new ArrayList<>());
        if (file.isDirectory()) {
            List<String> finalNames = names;
            Stream.of(file.listFiles()).forEach(f -> doGetJavaFileNames(finalNames, f, rootPath));
        } else if (file.isFile() && StringUtils.endsWith(file.getName(), suffix)) {
            String path = file.getPath();
            String result = StringUtils.substringBetween(path, rootPath, suffix).replace(File.separator, dot);
            if (StringUtils.startsWith(result, dot)) {
                result = result.substring(1);
            }
            if(result.contains(infoJava)){
                return;
            }
            names.add(result);
        }
    }


}
