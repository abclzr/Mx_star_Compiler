package Frontend;

public class UnaryInstruction extends IRInstruction {
    UnaryInstruction(op o) {
        super(o);
        assert o == op.UNARY;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
