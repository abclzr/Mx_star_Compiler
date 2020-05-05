package Frontend;

import AST.StatementNode;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {
    private CodeSegment enclosureCodeSegment;
    private List<StatementNode> stmtList;
    private List<IRInstruction> instList;
    private BasicBlock pre;
    private BasicBlock pos;
    private List<BasicBlock> pred;
    private List<BasicBlock> post;

    public BasicBlock(CodeSegment fa) {
        this.enclosureCodeSegment = fa;
        this.stmtList = new ArrayList<>();
        this.instList = new ArrayList<>();
        this.pred = new ArrayList<>();
        this.post = new ArrayList<>();
        this.pre = null;
        this.pos = null;
    }

    public BasicBlock split() {
        BasicBlock nb = new BasicBlock(enclosureCodeSegment);
        nb.pos = this.pos;
        nb.pre = this;
        this.pos = nb;
        return nb;
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
        if (a != null) a.post.add(this);
    }

    public void addPost(BasicBlock a) {
        post.add(a);
        if (a != null) a.pred.add(this);
    }
}
