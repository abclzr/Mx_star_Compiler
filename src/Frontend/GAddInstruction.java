package Frontend;

import Semantic.Type;

public class GAddInstruction extends IRInstruction {
    VirtualRegister lhs;//rhs is a pointer
    Address offset;
    private int width;

    GAddInstruction(IRInstruction.op o, VirtualRegister lhs, Address addr, Type tp) {
        super(o);
        assert o == op.GADD;
        this.lhs = lhs;
        this.offset = addr;
        this.width = tp.getWidth();
    }

    @Override
    public String getMessage() {
        return ("Op "  + lhs.getName() + " = bottom_stack_pointer + " + offset + " (" + width + " byte)");
    }
}
