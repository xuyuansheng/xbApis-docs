package cn.xuxiaobu.doc.apis.processor.note;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.definition.DefaultJavaApiDefinition;
import cn.xuxiaobu.doc.apis.initialization.JavaSourceFileContext;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 020102
 * @date 2019-07-30 15:50
 */
@Slf4j
public class JavaApiNoteProcessor implements ApiNoteProcessor {

    private JavaSourceFileContext sourceFileContext;


    public JavaApiNoteProcessor(JavaSourceFileContext sourceFileContext) {
        this.sourceFileContext = sourceFileContext;
    }

    @Override
    public void postNoteProcess(ApiDefinition definition) {
        if (!(definition instanceof DefaultJavaApiDefinition)) {
            return;
        }
        DefaultJavaApiDefinition defaultJavaApiDefinition = (DefaultJavaApiDefinition) definition;
        InputStream sourceStream;
        try {
          sourceStream=  defaultJavaApiDefinition.getJavaFileMateData().getInputStream();
        } catch (IOException e) {
            log.info("没有获取到java源码数据 ,{}",defaultJavaApiDefinition);
            log.error("没有获取到java源码数据  ",e);
            return;
        }
        ParseResult<CompilationUnit> parseResult = new JavaParser().parse(sourceStream);
        CompilationUnit compilationUnit = parseResult.getResult().orElse(new CompilationUnit());

        new JavaMethodVisitor(this.sourceFileContext).visit(compilationUnit, defaultJavaApiDefinition);
        try {
            sourceStream.close();
        } catch (IOException e) {
            log.error("流关闭失败  ",e);
        }

    }
}
