package cn.xuxiaobu.doc.util.processor;

import cn.xuxiaobu.doc.apis.definition.CustomTags;
import cn.xuxiaobu.doc.apis.enums.CustomTagType;
import cn.xuxiaobu.doc.util.regx.PatternInit;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * @author 020102
 * @date 2019-09-27 15:05
 */
public class CustomTagUtils {


    /**
     * 从文本中取出自定义标签
     *
     * @param content 文本内容  如:  方法1的参数1   @defaultValue(默认值) @defaultValue(默认值) @defaultValue(abcdse)
     * @return 取出标签后的内容  如: 方法1的参数1
     */
    public static String removeCustomTags(String content) {
        Matcher match = PatternInit.customTagPattern.matcher(content);
        String buffer = content;
        while (match.find()) {
            buffer = StringUtils.replaceOnce(buffer, match.group(), "");
        }
        return buffer;

    }

    public static void main(String[] args) {
        String content = "方法1的参数1   @defaultValue(默认值)dd@defaultValue(默认值) @defaultValue(abcdse)";
        String ac = CustomTagUtils.removeCustomTags(content);
        Optional<List<CustomTags>> res = CustomTagType.defaultValue.getTagFromString(content);
    }


}
