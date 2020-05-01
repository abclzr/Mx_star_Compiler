package Semantic;

import AST.ASTNode;
import Frontend.Address;
import Frontend.VirtualRegister;
import Utils.Position;

public class VariableSymbol extends Symbol {
    private VirtualRegister virtualRegister;
    private int offsetInClass;

    public void setOffsetInClass(int offsetInClass) {
        this.offsetInClass = offsetInClass;
    }

    public int getOffsetInClass() {
        return offsetInClass;
    }

    public VariableSymbol(Type tp, String na, ASTNode def, Position pos) {
        super(tp, na, def, pos);
    }

    public void setVirtualRegister(VirtualRegister virtualRegister) {
        this.virtualRegister = virtualRegister;
    }

    public VirtualRegister getVirtualRegister() {
        return virtualRegister;
    }

    @Override
    public boolean isVariableSymbol() {
        return true;
    }

    @Override
    public boolean isFunctionSymbol() {
        return false;
    }
}
