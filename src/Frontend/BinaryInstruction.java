package Frontend;

public class BinaryInstruction extends IRInstruction {
    private VirtualRegister lhs, rhs1, rhs2;
    private int width;
    private String bop;

    BinaryInstruction(op o, VirtualRegister l, VirtualRegister r1, String bop, VirtualRegister r2) {
        super(o);
        this.lhs = l;
        this.rhs1 = r1;
        this.bop = bop;
        this.rhs2 = r2;
        assert o == op.BINARY;
    }
}
