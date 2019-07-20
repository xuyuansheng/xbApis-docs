package cn.xuxiaobu.doc.exceptions;

/**
 * @author 020102
 * @date 2019-07-19 10:38
 */
public class InitSourceException extends BuildDocException {

    private String sourcePath;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public InitSourceException(String sourcePath) {
        super("初始化文件 name : " + sourcePath + "失败 ! ");
        this.sourcePath = sourcePath;
    }

    public InitSourceException(String sourcePath, String message) {
        super("初始化文件 name : " + sourcePath + "失败 ! " + message);
        this.sourcePath = sourcePath;
    }

    public InitSourceException(String sourcePath, Throwable cause) {
        super("初始化文件 name : " + sourcePath + "失败 ! ", cause);
        this.sourcePath = sourcePath;
    }

    public InitSourceException(String sourcePath, String message, Throwable cause) {
        super("初始化文件 name : " + sourcePath + "失败 ! " + message, cause);
        this.sourcePath = sourcePath;
    }


}
