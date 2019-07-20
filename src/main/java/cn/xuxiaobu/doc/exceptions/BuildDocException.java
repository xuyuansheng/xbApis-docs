package cn.xuxiaobu.doc.exceptions;

/**
 * 自定义异常
 *
 * @author 020102
 * @date 2019-07-19 10:28
 */
public class BuildDocException extends RuntimeException {


    public BuildDocException() {
    }

    public BuildDocException(String message) {
        super(message);
    }

    public BuildDocException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuildDocException(Throwable cause) {
        super(cause);
    }


}
