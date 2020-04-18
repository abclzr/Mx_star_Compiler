package Frontend;

import Semantic.FunctionSymbol;

import java.util.ArrayList;

public class CodeSegment {
    private FunctionSymbol functionSymbol;
    private ArrayList<IRInstruction> instList;
    private int storage;

    CodeSegment(FunctionSymbol inFunc) {
        this.functionSymbol = inFunc;
        this.instList = new ArrayList<>();
    }

    public int Allocate(int width) {
        storage += width;
        return storage - width;
    }
}
