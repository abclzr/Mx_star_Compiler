package Frontend;

import AST.StatementNode;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {
    private CodeSegment enclosureCodeSegment;
    private List<StatementNode> stmtList;
    private List<IRInstruction> instList;
    private List<BasicBlock> pred;
    private List<BasicBlock> post;

    public BasicBlock(CodeSegment fa) {
        this.enclosureCodeSegment = fa;
        this.stmtList = new ArrayList<>();
        this.instList = new ArrayList<>();
        this.pred = new ArrayList<>();
        this.post = new ArrayList<>();
    }

    public CodeSegment getEnclosureCodeSegment() {
        return enclosureCodeSegment;
    }

    public List<BasicBlock> getPost() {
        return post;
    }

    public List<BasicBlock> getPred() {
        return pred;
    }

    public List<IRInstruction> getInstList() {
        return instList;
    }

    public List<StatementNode> getStmtList() {
        return stmtList;
    }

    public void addStmtNode(StatementNode a) {
        stmtList.add(a);
    }

    public void addInst(IRInstruction a) {
        instList.add(a);
    }

    public void addPred(BasicBlock a) {
        pred.add(a);
    }

    public void addPost(BasicBlock a) {
        post.add(a);
    }
}
