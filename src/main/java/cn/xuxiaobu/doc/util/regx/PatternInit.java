package cn.xuxiaobu.doc.util.regx;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 正则表达式预编译类
 *
 * @author 020102
 * @date 2019-08-15 15:37
 */
public class PatternInit {
    /** 替换类名前面的所有包名和点号 */
    public static Pattern classNameReg = Pattern.compile(".*(?=\\.).");
    /**  去掉java类型的泛型, 如: java.lang.List<String> -> java.lang.List , Map<String,Object> -> Map  */
    public static Pattern classTypeGeneric = Pattern.compile("<.*>");
    /**  配置文本中的自定义tag */
    public static Pattern customTagPattern = Pattern.compile("\\s?@\\w+\\(.+?\\)\\s?");

    public static void main(String[] args) throws FileNotFoundException {
        Path path = Paths.get("D:\\javaWorkSpace\\xbapi-docs\\src\\main\\java\\cn\\xuxiaobu\\doc\\util\\regx\\PatternInit.java");
        String index = PatternInit.class.getName();
        index = NNN.MMM.class.getName();
        ClassOrInterfaceDeclaration re = getClassOrInterfaceDeclaration(index, path.toFile());
        String list = "java.lang.List<String>";
        String map = "Map<String,Object>";
        String a = StringUtils.replacePattern(list, PatternInit.classTypeGeneric.toString(), "");
        String b = StringUtils.replacePattern(map, PatternInit.classTypeGeneric.toString(), "");

    }


    public static ClassOrInterfaceDeclaration getClassOrInterfaceDeclaration(String className, File file) {

        final String split = "$";
        try {

            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(file);
            CompilationUnit compilationUnit = parseResult.getResult().orElse(new CompilationUnit());
            String simpleName = StringUtils.replacePattern(className, PatternInit.classNameReg.pattern(), "");

            if (StringUtils.contains(simpleName, split)) {
                String[] array = StringUtils.split(simpleName,split);
                ClassOrInterfaceDeclaration temp = null;
                for (int i = 0; i < array.length; i++) {
                    int finalI = i;
                    if (i == 0 && (temp = compilationUnit.getClassByName(array[i]).orElse(null)) != null) {
                        continue;
                    } else if (i != 0 && (temp = temp.getChildNodesByType(ClassOrInterfaceDeclaration.class).stream()
                            .filter(clazzUnit -> clazzUnit.getNameAsString().equals(array[finalI]))
                            .findFirst().orElse(null)) != null) {
                        continue;
                    } else {
                        break;
                    }
                }
                return temp!=null?temp:new ClassOrInterfaceDeclaration();
            } else {
                Optional<ClassOrInterfaceDeclaration> clazzSource = compilationUnit.getClassByName(simpleName);
                return clazzSource.orElse(new ClassOrInterfaceDeclaration());
            }
        } catch (Exception e) {

            return new ClassOrInterfaceDeclaration();
        }
    }


    class NNN {
        private String a;
        class MMM{}
    }
}
