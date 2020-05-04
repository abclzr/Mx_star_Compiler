package Frontend;

public class BinaryInstruction extends IRInstruction {
    private VirtualRegister lhs, rhs1, rhs2;
    private int imm_rhs2;
    private int width;
    private String bop;
    private boolean is_imm;

    BinaryInstruction(op o, VirtualRegister l, VirtualRegister r1, String bop, VirtualRegister r2) {
        super(o);
        this.lhs = l;
        this.rhs1 = r1;
        this.bop = bop;
        this.rhs2 = r2;
        assert o == op.BINARY;
        this.is_imm = false;
    }

    BinaryInstruction(op o, VirtualRegister l, VirtualRegister r1, String bop, int r2) {
        super(o);
        this.lhs = l;
        this.rhs1 = r1;
        this.bop = bop;
        this.imm_rhs2 = r2;
        assert o == op.BINARY;
        this.is_imm = true;
    }
}
