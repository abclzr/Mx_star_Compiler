package Frontend;

public abstract class IRInstruction {
    public op IRop;

    IRInstruction(op o) {
        this.IRop = o;
    }

    public abstract void codegen();

    public enum op {RETURN, MALLOC, BINARY, UNARY, COPY, JUMP, CJUMP, BRANCH, PARAM, CALL, ARRAY, LOAD, STORE, SLOAD, SSTORE, GLOAD, GSTORE, SADD, GADD};

    public abstract String getMessage();

    public void la(String rd, String symbol) {
        System.out.println("\tla " + rd + ", " + symbol);
    }
    public void lb(String rd, String symbol) {
        System.out.println("\tlb " + rd + ", " + symbol);
    }
    public void lh(String rd, String symbol) {
        System.out.println("\tlh " + rd + ", " + symbol);
    }
    public void lw(String rd, String symbol) {
        System.out.println("\tlw " + rd + ", " + symbol);
    }
    public void sb(String rd, String symbol, String rt) {
        System.out.println("\tsb " + rd + ", " + symbol + ", " + rt);
    }
    public void sh(String rd, String symbol, String rt) {
        System.out.println("\tsb " + rd + ", " + symbol + ", " + rt);
    }
    public void sw(String rd, String symbol, String rt) {
        System.out.println("\tsb " + rd + ", " + symbol + ", " + rt);
    }
    public void nop() {
        System.out.println("\tnop");
    }
    public void li(String rd, int imm) {
        System.out.println("\tli " + rd + ", " + imm);
    }
    public void mv(String rd, String rs) {
        System.out.println("\tmv " + rd + ", " + rs);
    }
    public void not(String rd, String rs) {
        System.out.println("\tnot " + rd + ", " + rs);
    }
    public void neg(String rd, String rs) {
        System.out.println("\tneg " + rd + ", " + rs);
    }
    public void seqz(String rd, String rs) {
        System.out.println("\tseqz " + rd + ", " + rs);
    }
    public void snez(String rd, String rs) {
        System.out.println("\tsnez " + rd + ", " + rs);
    }
    public void sltz(String rd, String rs) {
        System.out.println("\tsltz " + rd + ", " + rs);
    }
    public void sgtz(String rd, String rs) {
        System.out.println("\tsgtz " + rd + ", " + rs);
    }
    public void beqz(String rs, String offset) {
        System.out.println("\tbeqz " + rs + ", " + offset);
    }
    public void bnez(String rs, String offset) {
        System.out.println("\tbnez " + rs + ", " + offset);
    }
    public void blez(String rs, String offset) {
        System.out.println("\tblez " + rs + ", " + offset);
    }
    public void bgez(String rs, String offset) {
        System.out.println("\tbgez " + rs + ", " + offset);
    }
    public void bltz(String rs, String offset) {
        System.out.println("\tbltz " + rs + ", " + offset);
    }
    public void btz(String rs, String offset) {
        System.out.println("\tbtz " + rs + ", " + offset);
    }
    public void bgt(String rs, String rt, String offset) {
        System.out.println("\tbgt " + rs + ", " + rt + ", " + offset);
    }
    public void ble(String rs, String rt, String offset) {
        System.out.println("\tble " + rs + ", " + rt + ", " + offset);
    }
    public void bgtu(String rs, String rt, String offset) {
        System.out.println("\tbgtu " + rs + ", " + rt + ", " + offset);
    }
    public void bleu(String rs, String rt, String offset) {
        System.out.println("\tbleu " + rs + ", " + rt + ", " + offset);
    }
    public void j(String offset) {
        System.out.println("\tj " + offset);
    }
    public void jr(String rs) {
        System.out.println("\tjr " + rs);
    }
    public void ret() {
        System.out.println("\tret");
    }
    public void call(String offset) {
        System.out.println("\tcall " + offset);
    }
}