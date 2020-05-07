package Frontend;

import Semantic.Type;

public class GLoadInstruction extends IRInstruction {
    VirtualRegister lhs;//rhs is a pointer
    Address offset;
    private int width;

    GLoadInstruction(IRInstruction.op o, VirtualRegister lhs, Address addr, Type tp) {
        super(o);
        assert o == IRInstruction.op.GLOAD;
        this.lhs = lhs;
        this.offset = addr;
        this.width = tp.getWidth();
    }

    @Override
    public String getMessage() {
        return ("GLoad "  + lhs.getName() + ", bottom_stack_pointer + " + offset.getAddr() + " (" + width + " byte)");
    }
}
