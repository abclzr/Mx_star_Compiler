package AST;

import Utils.Position;

import java.util.List;

public class ExpressionNode extends ASTNode {
    String id, op;
    Type type;
    LiteralNode literal;
    ExpressionNode expr1, expr2;
    List<ExpressionNode> exprList;
    CreatorNode creator;

    public ExpressionNode(Position pos, Type tp, String id) {
        super(pos);
        this.type = tp;
        assert tp == Type.IDENTIFIER;
        this.id = id;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode e1, String op, ExpressionNode e2) {
        super(pos);
        this.type = tp;
        assert tp == Type.BINARY;
        this.expr1 = e1;
        this.op = op;
        this.expr2 = e2;
    }

    public ExpressionNode(Position pos, Type tp, String op, ExpressionNode e1) {
        super(pos);
        this.type = tp;
        assert tp == Type.PRE;
        this.op = op;
        this.expr1 = e1;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode e1, String op) {
        super(pos);
        this.type = tp;
        assert tp == Type.POST;
        this.expr1 = e1;
        this.op = op;
    }

    public ExpressionNode(Position pos, Type tp, LiteralNode li) {
        super(pos);
        this.type = tp;
        assert tp == Type.LITERAL;
        this.literal = li;
    }

    public ExpressionNode(Position pos, Type tp) {
        super(pos);
        this.type = tp;
        assert tp == Type.THIS;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode ex, String op, String id) {
        super(pos);
        this.type = tp;
        assert tp == Type.MEMBER && op.equals('.');
        this.id = id;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode ex1, ExpressionNode ex2) {
        super(pos);
        this.type = tp;
        assert tp == Type.ARRAY;
        this.expr1 = ex1;
        this.expr2 = ex2;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode ex1, List<ExpressionNode> list) {
        super(pos);
        this.type = tp;
        assert tp == Type.CALL;
        this.expr1 = ex1;
        this.exprList = list;
    }

    public ExpressionNode(Position pos, Type tp, CreatorNode cr) {
        super(pos);
        this.type = tp;
        assert tp == Type.NEW;
        this.creator = cr;
    }

    @Override
    public void accept(ASTVisitor visitor) {

    }

    enum Type {THIS, LITERAL, IDENTIFIER, MEMBER, ARRAY, CALL, NEW, POST, PRE, BINARY,}
}
