package Frontend;

public class ReturnInstruction extends IRInstruction {
    private VirtualRegister returnValue;
    ReturnInstruction(op o, VirtualRegister returnValue) {
        super(o);
        assert o == op.RETURN;
        this.returnValue = returnValue;
    }
}
