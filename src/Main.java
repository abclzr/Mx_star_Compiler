
import AST.ASTNode;
import AST.ASTBuilder;
import Parser.MxParser;
import Parser.MxLexer;
import java.io.FileInputStream;
import java.io.InputStream;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {
    public static ASTNode BuildAST(InputStream in) throws Exception {
        MxParser parser = new MxParser(new CommonTokenStream(new MxLexer(CharStreams.fromStream(in))));
        ParseTree root = parser.program();
        ASTBuilder a = new ASTBuilder();
        return a.visit(root);
    }

    public static void main(String... args) throws Exception {
        InputStream in = new FileInputStream("test.txt");
        ASTNode root = BuildAST(in);
    }
}