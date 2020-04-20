package Frontend;

import Semantic.Type;
import Semantic.VariableSymbol;

public class VirtualRegister {
    private CodeSegment inCodeSegment;
    private VariableSymbol varSymbol;
    private int value;
    private int width;
    private Type type;
    private int relativeAddress;
    private Address addr;

    VirtualRegister(CodeSegment inCode, Type tp) {
        this.inCodeSegment = inCode;
        this.value = 0;
        this.width = tp.getWidth();
        this.type = tp;
        this.relativeAddress = inCode.Allocate(this.width);
        addr = new Address();
    }

    public int getRelativeAddress() {
        return relativeAddress;
    }

    public CodeSegment getInCodeSegment() {
        return inCodeSegment;
    }

    public int getAddrValue() {
        return this.addr.getAddr();
    }

    public Address getAddr() {
        return addr;
    }

    public void setAddr(Address addr) {
        this.addr = addr;
    }
}
