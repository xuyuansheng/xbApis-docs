package cn.xuxiaobu.doc.export.postman.collections;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回
 *
 * @author 020102
 * @date 2019-09-25 14:42
 */
@Data
@Accessors(chain = true)
public class PostManResponse {

    public static List<PostManResponse> getPostManResponse() {
        return new ArrayList<>(0);
    }

}
