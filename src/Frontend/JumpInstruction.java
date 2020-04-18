package Frontend;

public class JumpInstruction extends IRInstruction {
    JumpInstruction(op o) {
        super(o);
        assert o == op.JUMP;
    }
}
