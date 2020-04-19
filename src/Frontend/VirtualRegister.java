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

    VirtualRegister(CodeSegment inCode, Type tp) {
        this.inCodeSegment = inCode;
        this.value = 0;
        this.width = tp.getWidth();
        this.type = tp;
        this.relativeAddress = inCode.Allocate(this.width);
    }
}
