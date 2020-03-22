package AST;

import Utils.Position;

public class StatementNode extends ASTNode {


    public StatementNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {

    }

    enum Type {BLOCK, IF, FOR, WHILE, RETURN, BREAK, CONTINUE, EXPR, VARDECL}
}
