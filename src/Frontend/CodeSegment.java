package Frontend;

import Semantic.FunctionSymbol;

import java.util.ArrayList;
import java.util.List;

public class CodeSegment {
    private FunctionSymbol functionSymbol;
    private List<IRInstruction> instList;
    private int stackStorage;
    private VirtualRegister thisPointer;

    public void setThisPointer(VirtualRegister thisPointer) {
        this.thisPointer = thisPointer;
    }

    public VirtualRegister getThisPointer() {
        return thisPointer;
    }

    CodeSegment(FunctionSymbol inFunc) {
        this.functionSymbol = inFunc;
        inFunc.setCodeSegment(this);
        this.instList = new ArrayList<>();
        stackStorage = 0;
        thisPointer = null;
    }

    public int Allocate(int width) {
        stackStorage += width;
        return stackStorage - width;
    }

    public void addIRInst(IRInstruction inst) {
        instList.add(inst);
    }
}
