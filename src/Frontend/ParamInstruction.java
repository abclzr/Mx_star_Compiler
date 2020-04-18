package Frontend;

public class ParamInstruction extends IRInstruction {
    ParamInstruction(op o) {
        super(o);
        assert o == op.PARAM;
    }
}
