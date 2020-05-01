package Frontend;

public abstract class IRInstruction {
    public op IRop;

    IRInstruction(op o) {
        this.IRop = o;
    }

    public enum op {BINARY, UNARY, COPY, JUMP, CJUMP, BRANCH, PARAM, CALL, ARRAY, LOAD, STORE, SLOAD, SSTORE, GLOAD, GSTORE};
}