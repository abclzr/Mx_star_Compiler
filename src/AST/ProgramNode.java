package AST;

import Utils.Position;
import java.util.List;

public class ProgramNode extends ASTNode {
    List<ASTNode> declNodeList;
    ProgramNode(Position pos, List<ASTNode> decls) {
        super(pos);
        this.declNodeList = decls;
    }

    @Override
    public void accept(ASTVisitor visitor) {
    }
}
