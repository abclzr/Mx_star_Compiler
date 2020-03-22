package AST;

import Utils.Position;

import java.util.List;

public class ClassDeclarationNode extends ASTNode {
    private String className;
    private List<ASTNode> varList;
    private List<ASTNode> funcList;

    public ClassDeclarationNode(Position pos, String name, List<ASTNode> vars, List<ASTNode> funcs) {
        super(pos);
        this.className = name;
        this.varList = vars;
        this.funcList = funcs;
    }

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
