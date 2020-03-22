package AST;

import Utils.Position;

import java.util.List;

public class CreatorNode extends ASTNode {
    private String id;
    private boolean isPrimitive;
    private List<ExpressionNode> expr;
    private int dimension;

    public CreatorNode(Position pos, String id, boolean isPrimitive, List<ExpressionNode>ex, int di) {
        super(pos);
        this.id = id;
        this.isPrimitive = isPrimitive;
        this.expr = ex;
        this.dimension = di;
    }

    public List<ExpressionNode> getExpr() {return expr;}
    public String getId() {return id;}
    public boolean getIsPrimitive() {return isPrimitive;}
    public int getDimension() {return dimension;}

    @Override
    public void accept(ASTVisitor visitor) {

    }
}
