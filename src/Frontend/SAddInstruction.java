package Frontend;

import Semantic.Type;

public class SAddInstruction extends IRInstruction {
    VirtualRegister lhs;//rhs is a pointer
    Address offset;
    private int width;

    SAddInstruction(IRInstruction.op o, VirtualRegister lhs, Address addr, Type tp) {
        super(o);
        assert o == IRInstruction.op.SADD;
        this.lhs = lhs;
        this.offset = addr;
        this.width = tp.getWidth();
    }
    SAddInstruction(IRInstruction.op o, VirtualRegister lhs, int addr, Type tp) {
        super(o);
        assert o == IRInstruction.op.SADD;
        this.lhs = lhs;
        this.offset = new Address();
        this.offset.setAddr(addr);
        this.width = tp.getWidth();
    }
}