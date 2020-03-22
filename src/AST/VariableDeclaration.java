package AST;

import Utils.Position;

public class VariableDeclaration extends ASTNode {
    VariableDeclaration(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
