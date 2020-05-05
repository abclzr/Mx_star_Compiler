package Frontend;

import Semantic.Type;

import java.util.List;

public class CallInstruction extends IRInstruction {
    VirtualRegister lhs;
    int width;
    CodeSegment callee;
    List<VirtualRegister> params;
    CallInstruction(op o, VirtualRegister lhs, Type tp, CodeSegment c, List<VirtualRegister> p) {
        super(o);
        assert o == op.CALL;
        this.lhs = lhs;
        this.width = tp.getWidth();
        this.callee = c;
        this.params = p;
    }
}
