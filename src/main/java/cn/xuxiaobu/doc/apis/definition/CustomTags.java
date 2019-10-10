package cn.xuxiaobu.doc.apis.definition;

import cn.xuxiaobu.doc.apis.enums.CustomTagType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 自定义的标签
 *
 * @author 020102
 * @date 2019-09-27 11:28
 */
@Data
@Accessors(chain = true)
public class CustomTags {

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 名称
     */
    private String name;

    /**
     * 标签值
     * 如: 方法1的参数1   @defaultValue(默认值)
     * 获取到的 value = 默认值
     */
    private String value;

    /**
     * 标签类型
     */
    private CustomTagType type;

}
