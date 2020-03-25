package AST;

import Utils.Position;

import java.util.List;

/*
variableDeclaration: type variableDecorator(',' variableDecorator)* ';';
 */

public class VarDeclNode extends ASTNode {
    private TypeNode type;
    private List<VarDecoratorNode> list;

    VarDeclNode(Position pos, TypeNode tp, List<VarDecoratorNode> li) {
        super(pos);
        this.type = tp;
        this.list = li;
    }

    public TypeNode getType() {
        return type;
    }

    public List<VarDecoratorNode> getVarDecoratorList() {
        return list;
    }

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
