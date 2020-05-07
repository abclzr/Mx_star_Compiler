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

    @Override
    public String getMessage() {
        if (jump_when_true)
            return "if (" + c.getName() + " == true) go to " + des.getName();
        else
            return "if (" + c.getName() + " == false) go to " + des.getName();
    }
}
