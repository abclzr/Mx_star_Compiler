package Frontend;

import Semantic.ClassType;
import Semantic.FunctionSymbol;

import java.util.ArrayList;
import java.util.List;

public class CodeSegment {
    private FunctionSymbol functionSymbol;
    private int stackStorage;
    private VirtualRegister thisPointer;
    private BasicBlock headBlock;
    private ClassType classType;
    private int vsNum;
    private VirtualRegister constructorReturnValue;

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setThisPointer(VirtualRegister thisPointer) {
        this.thisPointer = thisPointer;
    }

    public VirtualRegister getThisPointer() {
        return thisPointer;
    }

    CodeSegment(FunctionSymbol inFunc) {
        this.functionSymbol = inFunc;
        if (inFunc != null) inFunc.setCodeSegment(this);
        stackStorage = 0;
        thisPointer = null;
        vsNum = 0;
        this.headBlock = new BasicBlock(this);
    }

    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }

    public int Allocate(int width) {
        stackStorage += width;
        return stackStorage - width;
    }

    public int getBBNum() {
        return vsNum++;
    }

    public BasicBlock getHeadBlock() {
        return headBlock;
    }

    public void printall() {
        if (functionSymbol == null)
            System.out.println("Where everything starts:");
        else
            System.out.println(functionSymbol.getName() + ":");
        BasicBlock cs = headBlock;
        while (cs != null) {
            cs.printall();
            cs = cs.getPos();
        }
        System.out.println("");
    }

    public void setConstructorReturnValue(VirtualRegister th) {
        constructorReturnValue = th;
    }

    public VirtualRegister getConstructorReturnValue() {
        return constructorReturnValue;
    }
}
