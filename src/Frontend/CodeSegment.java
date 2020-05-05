package Frontend;

import Semantic.FunctionSymbol;

import java.util.ArrayList;
import java.util.List;

public class CodeSegment {
    private FunctionSymbol functionSymbol;
    private int stackStorage;
    private VirtualRegister thisPointer;
    private BasicBlock headBlock;

    public void setThisPointer(VirtualRegister thisPointer) {
        this.thisPointer = thisPointer;
    }

    public VirtualRegister getThisPointer() {
        return thisPointer;
    }

    CodeSegment(FunctionSymbol inFunc) {
        this.functionSymbol = inFunc;
        if (inFunc != null) inFunc.setCodeSegment(this);
        this.headBlock = new BasicBlock(this);
        stackStorage = 0;
        thisPointer = null;
    }

    public int Allocate(int width) {
        stackStorage += width;
        return stackStorage - width;
    }

    public BasicBlock getHeadBlock() {
        return headBlock;
    }
}
