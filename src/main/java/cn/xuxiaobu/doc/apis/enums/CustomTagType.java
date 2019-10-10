package cn.xuxiaobu.doc.apis.enums;

import cn.xuxiaobu.doc.apis.definition.CustomTags;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义的tag
 *
 * @author 020102
 * @date 2019-09-27 13:18
 */
public enum CustomTagType {

    /**
     * 默认值
     */
    defaultValue("defaultValue"),
    /**
     * 是否可以为 null
     */
    isNotNull("isNotNull");

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 匹配标签的正则
     */
    private Pattern tagRegx;

    CustomTagType(String tagName) {
        this.tagName = tagName;
        String pattern = "\\s?@" + tagName + "\\(.+?\\)\\s?";
        this.tagRegx = Pattern.compile(pattern);
    }

    /**
     * 从java注释内容中获取自定义标签
     * @param content 注释内容
     * @return 标签
     */
    public Optional<List<CustomTags>> getTagFromString(String content) {
        Matcher match = this.tagRegx.matcher(content);
        List<CustomTags> tags = new ArrayList<>();
        while (match.find()) {
            String tagStr = match.group();
            int start = tagStr.indexOf(this.tagName) + this.tagName.length() + 1;
            int end = tagStr.lastIndexOf(")");
            String value = StringUtils.substring(tagStr, start, end);
            tags.add(new CustomTags().setName(null).setValue(value).setTagName(this.tagName).setType(this));
        }
        if (tags.size() > 0) {
            return Optional.of(tags);
        } else {
            return Optional.empty();
        }
    }

    /**
     * 从JavaParse获取到的javadoc中获取自定义标签
     * @param javadoc  javadoc对象
     * @return 标签
     */
    public Optional<CustomTags> getTagFromJavaDoc(Javadoc javadoc) {
        Optional<JavadocBlockTag> blockTag = javadoc.getBlockTags().stream().filter(tag -> tag.getTagName().equals(this.tagName))
                .findFirst();
        if (blockTag.isPresent()) {
            String text = blockTag.get().getContent().toText();
            return Optional.of(new CustomTags().setName(null).setValue(text).setTagName(this.tagName).setType(this));
        }
        return Optional.empty();
    }

}
