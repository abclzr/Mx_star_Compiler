package Frontend;

import Semantic.Type;

public class StoreInstruction extends IRInstruction {
    private VirtualRegister addr, value;
    int offset;
    private int width;

    StoreInstruction(op o, VirtualRegister a, int addr, VirtualRegister b, Type tp) {
        super(o);
        assert o == op.STORE;
        this.addr = a;
        this.offset = addr;
        this.value = b;
        width = tp.getWidth();
    }

    @Override
    public void codegen() {
        lw("t1", addr.getAddrValue() + "(sp)");
        if (offset != 0)
            addi("t1", "t1", String.valueOf(offset));
        if (width == 4) {
            lw("t2", value.getAddrValue() + "(sp)");
            sw("t2", "t1", "t3");
        } else {
            lb("t2", value.getAddrValue() + "(sp)");
            sb("t2", "t1", "t3");
        }
    }

    @Override
    public String getMessage() {
        return ("Store "  + addr.getName() + " + " + offset + ", " + value.getName() + " (" + width + " byte)");
    }
}
