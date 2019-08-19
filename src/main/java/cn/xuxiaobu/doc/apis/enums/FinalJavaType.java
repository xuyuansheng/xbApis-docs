package cn.xuxiaobu.doc.apis.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
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

   final private static List<String> analysisIngClass=new ArrayList<>();

    FinalJavaType(Class<?> type) {
        this.type = type;
    }

    public static void add(Class<?> type){
        analysisIngClass.add(type.getName());
    }
    public static void remove(Class<?> type){
        analysisIngClass.remove(type.getName());
    }
    public static boolean exists(Class clazz) {
        /* java本身的类 */
        boolean javaClazz = StringUtils.startsWithAny(clazz.getTypeName(), "java.");
        if (javaClazz) {
            return true;
        }
        /* 正在解析中的类 */
        boolean analysisIngClassFlag = analysisIngClass.contains(clazz.getName());
        if(analysisIngClassFlag){
            return true;
        }
        /* 枚举中的类 */
        Optional<FinalJavaType> result = Stream.of(FinalJavaType.values())
                .filter(finalJavaType -> finalJavaType.type.isAssignableFrom(clazz))
                .findAny();
        return result.isPresent();
    }

}
