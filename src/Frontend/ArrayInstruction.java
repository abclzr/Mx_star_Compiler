package Frontend;

public class ArrayInstruction extends IRInstruction {
    ArrayInstruction(op o) {
        super(o);
        assert o == op.ARRAY;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
