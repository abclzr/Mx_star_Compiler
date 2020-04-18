package Frontend;

public class StoreInstrunction extends IRInstruction {
    StoreInstrunction(op o) {
        super(o);
        assert o == op.STORE;
    }
}
