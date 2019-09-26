package cn.xuxiaobu.doc.export.postman;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

/**
 * 信息描述
 *
 * @author 020102
 * @date 2019-09-25 14:23
 */
@Data
@Accessors(chain = true)
public class PostManInfo {
    private String _postman_id = UUID.randomUUID().toString().toLowerCase();
    private String name;
    private String schema = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json";
}
