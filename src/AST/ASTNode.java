package AST;

import Utils.Position;

public abstract class ASTNode {
    private Position pos;

    public ASTNode(Position pos) {this.pos = pos;}

    abstract public void accept(ASTVisitor visitor);

    public Position getPosition() {return pos;}
}
