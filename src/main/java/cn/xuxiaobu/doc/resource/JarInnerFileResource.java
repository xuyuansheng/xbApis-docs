package cn.xuxiaobu.doc.resource;

import org.springframework.core.io.AbstractResource;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * jar包内部文件resource
 *
 * @author 020102
 * @date 2019-07-18 14:26
 */
public class JarInnerFileResource extends AbstractResource {


    private JarFile jarFile;

    private String filePath;

    private JarEntry jarEntry;

    public JarInnerFileResource(JarFile jarFile, JarEntry jarEntry) {
        this.jarFile = jarFile;
        this.filePath = StringUtils.cleanPath(jarFile.getName());
        this.jarEntry = jarEntry;
    }

    /**
     * Return a description for this resource,
     * to be used for error output when working with the resource.
     * <p>Implementations are also encouraged to return this value
     * from their {@code toString} method.
     *
     * @see Object#toString()
     */
    @Override
    public String getDescription() {
        return filePath + "/" + jarEntry.getName();
    }

    /**
     * Return an {@link InputStream} for the content of an underlying resource.
     * <p>It is expected that each call creates a <i>fresh</i> stream.
     * <p>This requirement is particularly important when you consider an API such
     * as JavaMail, which needs to be able to read the stream multiple times when
     * creating mail attachments. For such a use case, it is <i>required</i>
     * that each {@code getInputStream()} call returns a fresh stream.
     *
     * @return the input stream for the underlying resource (must not be {@code null})
     * @throws FileNotFoundException if the underlying resource doesn't exist
     * @throws IOException           if the content stream could not be opened
     */
    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = jarFile.getInputStream(jarEntry);
        return inputStream;
    }
}
