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
    public String getMessage() {
        return ("Store "  + addr.getName() + " + " + offset + ", " + value.getName() + " (" + width + " byte)");
    }
}
