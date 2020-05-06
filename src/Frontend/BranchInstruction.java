package Frontend;

public class BranchInstruction extends IRInstruction {
    VirtualRegister r1, r2;
    String bop;
    BasicBlock toBB;

    BranchInstruction(op o, VirtualRegister r1, String bop, VirtualRegister r2, BasicBlock toBB) {
        super(o);
        assert o == op.BRANCH;
        this.r1 = r1;
        this.bop = bop;
        this.r2 = r2;
        this.toBB = toBB;
    }
}
