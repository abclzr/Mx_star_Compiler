package Frontend;

import Semantic.Type;

public class GStoreInstruction extends IRInstruction {
    private VirtualRegister value;
    private String gv;
    private int width;

    GStoreInstruction(IRInstruction.op o, String gv, VirtualRegister b, Type tp) {
        super(o);
        assert o == IRInstruction.op.GSTORE;
        this.gv = gv;
        this.value = b;
        width = tp.getWidth();
    }

    @Override
    public void replace_lhs_with(VirtualRegister a, VirtualRegister b) {
        assert false;
    }

    @Override
    public void codegen() {
        la("t1", gv);
        if (value.getWidth() == 4) {
            LW("t2", value.getAddrValue(), "sp");
            sw("t2", "0(t1)");
        } else {
            LB("t2", value.getAddrValue(), "sp");
            sb("t2", "0(t1)");
        }
    }

    @Override
    public void optimize() {
        value.read_ex(this);
    }

    @Override
    public String getMessage() {
        return ("GStore " + gv + ", " + value.getName() + " (" + width + " byte)");
    }
}
