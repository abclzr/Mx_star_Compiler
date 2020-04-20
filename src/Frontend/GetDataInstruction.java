package Frontend;

import Semantic.Type;

public class GetDataInstruction extends IRInstruction {
    VirtualRegister lhs, rhs;//rhs is a pointer
    private int width;
    GetDataInstruction(op o, VirtualRegister lhs, VirtualRegister rhs, Type tp) {
        super(o);
        assert o == op.GET;
        this.width = tp.getWidth();
    }
}
