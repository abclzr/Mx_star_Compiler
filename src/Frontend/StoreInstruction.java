package Frontend;

import Semantic.Type;

public class StoreInstruction extends IRInstruction {
    private VirtualRegister addr, value;
    private int width;

    StoreInstruction(op o, VirtualRegister a, VirtualRegister b, Type tp) {
        super(o);
        this.addr = a;
        this.value = b;
        width = tp.getWidth();
        assert o == op.STORE;
    }
}
