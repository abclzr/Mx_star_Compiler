package Frontend;

import Semantic.Type;

public class SLoadInstruction extends IRInstruction {
    VirtualRegister lhs;//rhs is a pointer
    Address offset;
    private int width;

    SLoadInstruction(IRInstruction.op o, VirtualRegister lhs, Address addr, Type tp) {
        super(o);
        assert o == IRInstruction.op.SLOAD;
        this.lhs = lhs;
        this.offset = addr;
        this.width = tp.getWidth();
    }
}
