package AST;

import Utils.Position;

import java.util.List;

/*
methodDeclaration: (typeOrVoid)? Identifier parameters block;
 */

public class MethodDeclNode extends ASTNode {
    private TypeOrVoidNode typeOrVoid;
    private String identifier;
    private List<ParameterNode> parameterList;
    private BlockNode block;
    private boolean isConstructor;

    MethodDeclNode(Position pos, TypeOrVoidNode tp, String id, List<ParameterNode> li, BlockNode bl, boolean is) {
        super(pos);
        this.typeOrVoid = tp;
        this.identifier= id;
        this.parameterList = li;
        this.block = bl;
        this.isConstructor = is;
    }

    public TypeOrVoidNode getTypOrVoid() {
        return typeOrVoid;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<ParameterNode> getParameterList() {
        return parameterList;
    }

    public BlockNode getBlock() {
        return block;
    }

    public boolean isConstructor() {
        return isConstructor;
    }

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
