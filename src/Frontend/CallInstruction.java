package Frontend;

public class CallInstruction extends IRInstruction {
    CallInstruction(op o) {
        super(o);
        assert o == op.CALL;
    }
}
