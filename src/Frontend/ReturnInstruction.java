package Frontend;

public class ReturnInstruction extends IRInstruction {
    private VirtualRegister returnValue;
    ReturnInstruction(op o, VirtualRegister returnValue) {
        super(o);
        assert o == op.RETURN;
        this.returnValue = returnValue;//May be Null!
    }

    @Override
    public void codegen() {
        lw("ra", returnValue.getInCodeSegment().getRaPointer().getAddrValue() + "(sp)");
        if (returnValue != null)
            if (returnValue.getWidth() == 4)
                lw("a0", returnValue.getAddrValue() + "(sp)");
            else
                lb("a0", returnValue.getAddrValue() + "(sp)");
        ret();
    }

    @Override
    public String getMessage() {
        if (returnValue == null)
            return "Return";
        else
            return ("Return " + returnValue.getName());
    }
}
