package AST;

import Utils.Position;

public class FunctionDeclaration extends ASTNode {
    FunctionDeclaration(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
