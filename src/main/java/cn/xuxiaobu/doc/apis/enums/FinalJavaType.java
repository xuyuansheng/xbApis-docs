package cn.xuxiaobu.doc.apis.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @program: xbapi-docs
 * @description: 最终的java类型, 不需要继续解析
 * @author: Mr.Xu
 * @create: 2019/8/4 12:07
 */
public enum FinalJavaType {
    /**
     * 基础数据类型
     */
    BYTE(byte.class),
    /**
     * 基础数据类型
     */
    SHORT(short.class),
    /**
     * 基础数据类型
     */
    INT(int.class),
    /**
     * 基础数据类型
     */
    LONG(long.class),
    /**
     * 基础数据类型
     */
    FLOAT(float.class),
    /**
     * 基础数据类型
     */
    DOUBLE(double.class),
    /**
     * 基础数据类型
     */
    BOOLEAN(boolean.class),
    /**
     * 基础数据类型
     */
    CHAR(char.class),
    /**
     * 集合类
     */
    COLLECTIONS(Collection.class),
    MAPS(Map.class),
    /**
     * 数字类
     */
    NUMBERS(Number.class);


    private Class<?> type;

    FinalJavaType(Class<?> type) {
        this.type = type;
    }

    public static boolean exists(Class clazz) {
        boolean javaClazz = StringUtils.startsWithAny(clazz.getTypeName(), "java.");
        if (javaClazz) {
            return true;
        }

        Optional<FinalJavaType> result = Stream.of(FinalJavaType.values())
                .filter(finalJavaType -> finalJavaType.type.isAssignableFrom(clazz))
                .findAny();
        return result.isPresent();
    }

}
