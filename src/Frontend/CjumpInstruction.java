package Frontend;

public class CjumpInstruction extends IRInstruction {
    CjumpInstruction(op o) {
        super(o);
        assert o == op.CJUMP;
    }
}
