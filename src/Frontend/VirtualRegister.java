package Frontend;

import Semantic.VariableSymbol;

public class VirtualRegister {
    private CodeSegment inCodeSegment;
    private VariableSymbol varSymbol;
    private int value;
    private int width;
    private int relativeAddress;

    VirtualRegister(CodeSegment inCode, int width) {
        this.inCodeSegment = inCode;
        this.width = width;
        this.value = 0;
        this.relativeAddress = inCode.Allocate(width);
    }
}
