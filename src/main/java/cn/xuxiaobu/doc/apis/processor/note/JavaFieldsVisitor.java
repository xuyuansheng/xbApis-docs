package cn.xuxiaobu.doc.apis.processor.note;

import cn.xuxiaobu.doc.apis.definition.TypeShowDefinition;
import cn.xuxiaobu.doc.apis.enums.CustomTagType;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @program: xbapi-docs
 * @description: 属性访问器类, 用来访问JavaParser解析后的CompilationUnit, 从中获取属性的定义, 注释等数据
 * @author: Mr.Xu
 * @create: 2019/8/9 22:29
 */
@Slf4j
public class JavaFieldsVisitor extends VoidVisitorAdapter<TypeShowDefinition> {

    @Override
    public void visit(ClassOrInterfaceDeclaration n, TypeShowDefinition arg) {
        Optional<FieldDeclaration> fieldOpt = n.getFieldByName(arg.getName());
        if (fieldOpt.isPresent()) {
            fieldOpt.get().accept(this, arg);
        } else {
            log.info("找不到对应的字段,ClassOrInterfaceDeclaration={} \n TypeShowDefinition={}", n.getFields(), arg);
        }
    }

    @Override
    public void visit(FieldDeclaration n, TypeShowDefinition arg) {
        /* 描述获取 */
        StringBuffer defaultValueOnDoc = new StringBuffer();
        n.getJavadoc().ifPresent(javadoc -> {
            String fieldDes = javadoc.getDescription().toText();
            arg.setDescription(fieldDes);
            CustomTagType.defaultValue.getTagFromJavaDoc(javadoc).ifPresent(defV -> {
                defaultValueOnDoc.append(defV);
            });
        });
        /*  默认值获取 */
        n.getVariables().get(0).getInitializer().ifPresent(initV -> {
            /*  获取字段初始化的值作为默认值 */
            String value = null;
            if (initV.isLiteralStringValueExpr()) {
                value = initV.asLiteralStringValueExpr().getValue();
            } else if (initV.isBooleanLiteralExpr()) {
                value = String.valueOf(initV.asBooleanLiteralExpr().getValue());
            }
            arg.setDefaultValue(value);
        });
        if (StringUtils.isBlank(arg.getDefaultValue())) {
            arg.setDefaultValue(defaultValueOnDoc.toString());
        }
    }
}
