package AST;

import Utils.Position;

/*
variableDecorator: Identifier ( '=' expression)?;
 */

public class VarDecoratorNode extends ASTNode {
    private String id;
    private ExpressionNode expr;

    public VarDecoratorNode(Position pos, String i, ExpressionNode e) {
        super(pos);
        this.id = i;
        this.expr = e;
    }

    public String getIdentifier() {
        return id;
    }

    public boolean hasExpr() {
        return expr != null;
    }

    public ExpressionNode getExpr() {
        return expr;
    }

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
