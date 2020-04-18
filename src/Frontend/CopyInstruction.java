package Frontend;

public class CopyInstruction extends IRInstruction {
    CopyInstruction(op o) {
        super(o);
        assert o == op.COPY;
    }
}
