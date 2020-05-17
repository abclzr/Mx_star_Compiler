package Frontend;

import Semantic.Type;

public class GLoadInstruction extends IRInstruction {
    VirtualRegister lhs;//rhs is a pointer
    String gv;
    private int width;

    GLoadInstruction(IRInstruction.op o, VirtualRegister lhs, String gv, Type tp) {
        super(o);
        assert o == IRInstruction.op.GLOAD;
        this.lhs = lhs;
        this.gv = gv;
        this.width = tp.getWidth();
    }

    @Override
    public String getMessage() {
        return ("GLoad "  + lhs.getName() + ", " + gv + " (" + width + " byte)");
    }
}
