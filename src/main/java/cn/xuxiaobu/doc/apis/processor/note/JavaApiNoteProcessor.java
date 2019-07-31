package cn.xuxiaobu.doc.apis.processor.note;

import cn.xuxiaobu.doc.apis.definition.ApiDefinition;
import cn.xuxiaobu.doc.apis.definition.DefaultJavaApiDefinition;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 020102
 * @date 2019-07-30 15:50
 */
public class JavaApiNoteProcessor implements ApiNoteProcessor {
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
            e.printStackTrace();
            return;
        }
        ParseResult<CompilationUnit> parseResult = new JavaParser().parse(sourceStream);
        CompilationUnit compilationUnit = parseResult.getResult().orElse(new CompilationUnit());

        new JavaMethodVisitor().visit(compilationUnit, defaultJavaApiDefinition);

        try {
            sourceStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
