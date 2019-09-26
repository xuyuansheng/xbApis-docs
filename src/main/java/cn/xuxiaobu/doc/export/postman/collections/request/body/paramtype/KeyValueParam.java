package cn.xuxiaobu.doc.export.postman.collections.request.body.paramtype;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 键值对参数形式
 *
 * @author 020102
 * @date 2019-09-25 16:01
 */
@Data
@Accessors(chain = true)
public class KeyValueParam {

    /**
     * key
     */
    private String key;
    /**
     * 描述
     */
    private String description;
    /**
     * 值的类型 text , file
     */
    private String type;
    /**
     * type 为 text 时 value有值
     */
    private String value;
    /**
     * type 为 file 时 src有值
     */
    private String src;

}
