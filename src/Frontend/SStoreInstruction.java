package Frontend;

import Semantic.Type;

public class SStoreInstruction extends IRInstruction {
    private VirtualRegister value;
    private Address offset;
    private int width;

    SStoreInstruction(IRInstruction.op o, Address addr, VirtualRegister b, Type tp) {
        super(o);
        assert o == IRInstruction.op.SSTORE;
        this.offset = addr;
        this.value = b;
        width = tp.getWidth();
    }
}
