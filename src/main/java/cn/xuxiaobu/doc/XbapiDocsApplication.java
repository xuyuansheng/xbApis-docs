package cn.xuxiaobu.doc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuyuansheng
 */
@SpringBootApplication
@RestController
public class XbapiDocsApplication {

    public static void main(String[] args) {
        SpringApplication.run(XbapiDocsApplication.class, args);
    }

    @RequestMapping("/{name}")
    public String hello(@PathVariable String name){
        return "你好,我是 "+name;
    }

}
