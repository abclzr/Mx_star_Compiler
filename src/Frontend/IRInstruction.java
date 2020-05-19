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
    public void sb(String rd, String symbol) {
        System.out.println("\tsb " + rd + ", " + symbol);
    }
    public void sh(String rd, String symbol) {
        System.out.println("\tsh " + rd + ", " + symbol);
    }
    public void sw(String rd, String symbol) {
        System.out.println("\tsw " + rd + ", " + symbol);
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

//                    case "+":
    public void add(String rd, String r1, String r2) {
        System.out.println("\tadd " + rd + ", " + r1 + ", " + r2);
    }
    public void addi(String rd, String r1, String r2) {
        System.out.println("\taddi " + rd + ", " + r1 + ", " + r2);
    }
//                    case "<=":
//                    case ">=":
//                    case "<":
//                    case ">":
    public void slt(String rd, String r1, String r2) {
        System.out.println("\tslt " + rd + ", " + r1 + ", " + r2);
    }
    public void slti(String rd, String r1, String r2) {
        System.out.println("\tslti " + rd + ", " + r1 + ", " + r2);
    }
//                    case "-":
    public void sub(String rd, String r1, String r2) {
        System.out.println("\tsub " + rd + ", " + r1 + ", " + r2);
    }
//                    case "*":
    public void mul(String rd, String r1, String r2) {
        System.out.println("\tmul " + rd + ", " + r1 + ", " + r2);
    }
//                    case "/":
    public void div(String rd, String r1, String r2) {
        System.out.println("\tdiv " + rd + ", " + r1 + ", " + r2);
    }
//                    case "%":
    public void rem(String rd, String r1, String r2) {
        System.out.println("\trem " + rd + ", " + r1 + ", " + r2);
    }
//                    case "<<":
    public void sll(String rd, String r1, String r2) {
        System.out.println("\tsll " + rd + ", " + r1 + ", " + r2);
    }
    public void slli(String rd, String r1, String r2) {
        System.out.println("\tslli " + rd + ", " + r1 + ", " + r2);
    }
//                    case ">>":
    public void sra(String rd, String r1, String r2) {
        System.out.println("\tsra " + rd + ", " + r1 + ", " + r2);
    }
    public void srai(String rd, String r1, String r2) {
        System.out.println("\tsrai " + rd + ", " + r1 + ", " + r2);
    }
//                    case "&":
    public void and(String rd, String r1, String r2) {
        System.out.println("\tand " + rd + ", " + r1 + ", " + r2);
    }
    public void andi(String rd, String r1, String r2) {
        System.out.println("\tandi " + rd + ", " + r1 + ", " + r2);
    }
//                    case "^":
    public void xor(String rd, String r1, String r2) {
        System.out.println("\txor " + rd + ", " + r1 + ", " + r2);
    }
    public void xori(String rd, String r1, String r2) {
        System.out.println("\txori " + rd + ", " + r1 + ", " + r2);
    }
//                    case "|":
    public void or(String rd, String r1, String r2) {
        System.out.println("\tor " + rd + ", " + r1 + ", " + r2);
    }
    public void ori(String rd, String r1, String r2) {
        System.out.println("\tori " + rd + ", " + r1 + ", " + r2);
    }
}