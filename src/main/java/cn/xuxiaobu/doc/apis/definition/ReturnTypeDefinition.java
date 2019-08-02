package cn.xuxiaobu.doc.apis.definition;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 返回值类型定义
 *
 * @author 020102
 * @date 2019-08-01 13:54
 */
@Data
@Accessors(chain = true)
public class ReturnTypeDefinition {

    /** 根描述,如果为简单类型时即为返回值的描述 */
    private String  rootDescription;

    private String genericity;


}
