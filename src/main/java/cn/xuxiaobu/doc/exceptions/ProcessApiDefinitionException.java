package cn.xuxiaobu.doc.exceptions;

/**
 * @author 020102
 * @date 2019-07-20 13:40
 */
public class ProcessApiDefinitionException extends BuildDocException {

    private String clazzAndMethodName;

    public ProcessApiDefinitionException(String clazzAndMethodName) {
        super("处理API定义出错 : definitionName-> "+clazzAndMethodName);
        this.clazzAndMethodName = clazzAndMethodName;

    }

    public ProcessApiDefinitionException(String message, String clazzAndMethodName) {
        super("{处理API定义出错 : definitionName-> "+clazzAndMethodName+"}  :"+message);
        this.clazzAndMethodName = clazzAndMethodName;
    }

    public ProcessApiDefinitionException(String message, Throwable cause, String clazzAndMethodName) {
        super("{处理API定义出错 : definitionName-> "+clazzAndMethodName+"}  :"+message,cause);
        this.clazzAndMethodName = clazzAndMethodName;
    }

    public ProcessApiDefinitionException(Throwable cause, String clazzAndMethodName) {
        super("处理API定义出错 : definitionName-> "+clazzAndMethodName,cause);
        this.clazzAndMethodName = clazzAndMethodName;
    }
}
