package AST;

import Utils.Position;
import java.util.List;

public class ProgramNode extends ASTNode {
    private List<ClassDeclNode> classDeclNodeList;
    private List<FuncDeclNode> funcDeclNodeList;
    private List<VarDeclNode> varDeclNodeList;

    ProgramNode(Position pos, List<ClassDeclNode> l1, List<FuncDeclNode> l2, List<VarDeclNode> l3) {
        super(pos);
        this.classDeclNodeList = l1;
        this.funcDeclNodeList = l2;
        this.varDeclNodeList = l3;
    }

    public List<ClassDeclNode> getClassDeclNodeList() {
        return classDeclNodeList;
    }

    public List<FuncDeclNode> getFuncDeclNodeList() {
        return funcDeclNodeList;
    }

    public List<VarDeclNode> getVarDeclNodeList() {
        return varDeclNodeList;
    }

    @Override
    public void accept(ASTVisitor visitor) {
    }
}
