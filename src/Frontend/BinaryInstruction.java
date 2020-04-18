package Frontend;

public class BinaryInstruction extends IRInstruction {
    BinaryInstruction(op o) {
        super(o);
        assert o == op.BINARY;
    }
}
