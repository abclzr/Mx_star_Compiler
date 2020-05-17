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
        this.rhs1 = r1;//may be null!
        this.bop = bop;
        this.rhs2 = r2;
        assert o == op.BINARY;
        this.is_imm = false;
    }

    BinaryInstruction(op o, VirtualRegister l, VirtualRegister r1, String bop, int r2) {
        super(o);
        this.lhs = l;
        this.rhs1 = r1;//may be null!
        this.bop = bop;
        this.imm_rhs2 = r2;
        assert o == op.BINARY;
        this.is_imm = true;
    }

    @Override
    public void codegen() {

    }

    @Override
    public String getMessage() {
        if (rhs1 != null) {
            if (is_imm)
                return lhs.getName() + " = " + rhs1.getName() + " " + bop + " " + imm_rhs2;
            else
                return lhs.getName() + " = " + rhs1.getName() + " " + bop + " " + rhs2.getName();
        } else {
            if (is_imm)
                return lhs.getName() + " = " + 0 + " " + bop + " " + imm_rhs2;
            else
                return lhs.getName() + " = " + 0 + " " + bop + " " + rhs2.getName();
        }
    }
}
