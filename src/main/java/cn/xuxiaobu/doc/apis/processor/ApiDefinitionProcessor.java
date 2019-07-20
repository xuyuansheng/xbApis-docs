package cn.xuxiaobu.doc.apis.processor;

/**
 * API定义处理器
 *
 * @author 020102
 * @date 2019-07-20 09:04
 */

public interface ApiDefinitionProcessor {
    /**
     * 处理器的执行优先级,数字越小 越先执行  默认: 10
     * @return
     */
    default Integer getOrder() {
        return 10;
    }
}
