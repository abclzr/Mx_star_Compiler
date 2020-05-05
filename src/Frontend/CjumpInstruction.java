package Frontend;

public class CjumpInstruction extends IRInstruction {
    private VirtualRegister c;
    private boolean jump_when_true;
    private BasicBlock des;

    CjumpInstruction(op o, VirtualRegister c, boolean jwt, BasicBlock des) {
        super(o);
        assert o == op.CJUMP;
        this.c = c;
        this.des = des;
        this.jump_when_true = jwt;
    }
}
