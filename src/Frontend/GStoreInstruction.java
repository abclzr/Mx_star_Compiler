package Frontend;

import Semantic.Type;

public class GStoreInstruction extends IRInstruction {
    private VirtualRegister value;
    private Address offset;
    private int width;

    GStoreInstruction(IRInstruction.op o, Address addr, VirtualRegister b, Type tp) {
        super(o);
        assert o == IRInstruction.op.GSTORE;
        this.offset = addr;
        this.value = b;
        width = tp.getWidth();
    }

    @Override
    public String getMessage() {
        return ("GStore bottom_stack_pointer + " + offset.getAddr() + ", " + value.getName() + " (" + width + " byte)");
    }
}
