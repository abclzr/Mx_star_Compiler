package Frontend;

public class UnaryInstruction extends IRInstruction {
    UnaryInstruction(op o) {
        super(o);
        assert o == op.UNARY;
    }
}
