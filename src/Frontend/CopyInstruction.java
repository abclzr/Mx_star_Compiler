package Frontend;

public class CopyInstruction extends IRInstruction {
    private VirtualRegister lhs, rhs;
    private int rhs_int;
    private Address rhs_addr;
    private type tp;

    public CopyInstruction(op o, VirtualRegister a, VirtualRegister b) {
        super(o);
        assert o == op.COPY;
        this.lhs = a;
        this.rhs = b;
        this.tp = type.reg_to_reg;
    }

    public CopyInstruction(op o, VirtualRegister a, int b) {
        super(o);
        assert o == op.COPY;
        this.lhs = a;
        this.rhs_int = b;
        this.tp = type.val_to_reg;
    }

    public CopyInstruction(op o, VirtualRegister a, Address b) {
        super(o);
        assert o == op.COPY;
        this.lhs = a;
        this.rhs_addr = b;
        this.tp = type.addr_to_reg;
    }

    public enum type {reg_to_reg, val_to_reg, addr_to_reg};
}
