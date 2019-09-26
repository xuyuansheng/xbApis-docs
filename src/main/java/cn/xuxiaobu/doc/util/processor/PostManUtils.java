package cn.xuxiaobu.doc.util.processor;

import cn.xuxiaobu.doc.export.postman.PostManApiJson;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @program: xbapi-docs
 * @description: postMan工具类
 * @author: Mr.Xu
 * @create: 2019/9/26 19:58
 */
@Slf4j
public class PostManUtils {
    /**
     * 输出到文件
     * @param path  文件路径
     * @param postManApiJson  api
     */
    public static void outPutToFile(String path, PostManApiJson postManApiJson) {
        /* 输出文件 */
        Path filePath = Paths.get(path);
        Path parent = filePath.getParent();
        if (parent != null && !parent.toFile().exists()) {
            parent.toFile().mkdirs();
        }

        try {
            FileWriter fileWriter = new FileWriter(new File(path));
            fileWriter.write(JSON.toJSONString(postManApiJson));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            log.error("生成json文件出错", e);
        }

    }


    public static void main(String[] args) {
        try {
            FileWriter fileWriter = new FileWriter(new File("D:\\JavaWorkSpace\\xbapi-docs\\target\\index.json"));
            fileWriter.write("{\"name\":\"collectionsName\",\"postman_id\":\"309312d3-70ed-4119-bfc0-24eb50a3d189\",\"schema\":\"https://schema.getpostman.com/json/collection/v2.1.0/collection.json\"}");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
