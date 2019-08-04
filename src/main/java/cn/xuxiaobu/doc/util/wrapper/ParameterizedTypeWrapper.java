package cn.xuxiaobu.doc.util.wrapper;

import cn.xuxiaobu.doc.apis.processor.note.TypeWrapper;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @program: xbapi-docs
 * @description: ParameterizedType包装类
 * @author: Mr.Xu
 * @create: 2019/8/4 10:22
 */
public class ParameterizedTypeWrapper implements TypeWrapper {


    private ParameterizedType type;

    public ParameterizedTypeWrapper(ParameterizedType type) {
        this.type = type;
    }

    @Override
    public Map<String, TypeWrapper> getFieldsType() {
        Map<String, TypeWrapper> result = WrapperUtils.getInstance(this.type.getRawType()).getFieldsType();
        return result;
    }

    @Override
    public boolean ifArray() {
        return WrapperUtils.getInstance(this.type.getRawType()).ifArray();
    }

    @Override
    public boolean ifArrayOrCollection() {
        return WrapperUtils.getInstance(this.type.getRawType()).ifArrayOrCollection();
    }

    @Override
    public String getSimpleName() {
        StringBuilder result = new StringBuilder();
        String rawType = WrapperUtils.getInstance(this.type.getRawType()).getSimpleName();
        result.append(rawType).append("<");
        Stream.of(this.type.getActualTypeArguments()).forEach(ar -> {
            String actualArgument = WrapperUtils.getInstance(ar).getSimpleName();
            result.append(actualArgument).append(",");
        });
        result.append(">");
        return StringUtils.replace(result.toString(), ",>", ">");
    }

    @Override
    public String getTypeName() {
        return this.type.getTypeName();
    }

    @Override
    public String getDefaultValue() {
        return null;
    }

    @Override
    public boolean ifFinalType() {
      return   WrapperUtils.getInstance(this.type.getRawType()).ifFinalType();
    }
}
