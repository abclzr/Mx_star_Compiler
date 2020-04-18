package Frontend;

public class BranchInstruction extends IRInstruction {
    BranchInstruction(op o) {
        super(o);
        assert o == op.BRANCH;
    }
}
