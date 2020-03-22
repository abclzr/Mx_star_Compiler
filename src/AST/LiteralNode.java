package AST;

import Utils.Position;

public class LiteralNode extends ASTNode {
    private literalType type;
    private String str;
    private int num;

    LiteralNode(Position pos, literalType tp, String s, Integer a) {
        super(pos);
        type = tp;
        str = s;
        num = a;
    }

    @Override
    public void accept(ASTVisitor visitor) {

    }

    enum literalType {STRING, INT, BOOL, NULL};
}
