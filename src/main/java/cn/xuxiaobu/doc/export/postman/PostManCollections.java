package cn.xuxiaobu.doc.export.postman;

import cn.xuxiaobu.doc.export.postman.collections.PostManProtocolProfileBehavior;
import cn.xuxiaobu.doc.export.postman.collections.PostManRequest;
import cn.xuxiaobu.doc.export.postman.collections.PostManResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * (目录)
 *
 * @author 020102
 * @date 2019-09-25 14:23
 */
@Data
@Accessors(chain = true)
public class PostManCollections {

    private String name;

    private List<PostManCollections> item;

    private PostManRequest request;

    private List<PostManResponse> response;

    private PostManProtocolProfileBehavior protocolProfileBehavior;
}
