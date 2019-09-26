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
public class RawParam {

    private Raw raw;

    @Data
    class Raw {
        String language;
    }

}
