package Frontend;

public class ReturnInstruction extends IRInstruction {
    private VirtualRegister returnValue;
    ReturnInstruction(op o, VirtualRegister returnValue) {
        super(o);
        assert o == op.RETURN;
        this.returnValue = returnValue;//May be Null!
    }

    @Override
    public String getMessage() {
        if (returnValue == null)
            return "Return";
        else
            return ("Return " + returnValue.getName());
    }
}
