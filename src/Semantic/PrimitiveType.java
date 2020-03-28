package Semantic;

import Utils.Position;
import Utils.SemanticError;

public class PrimitiveType extends Type {
    private Scope scope;
    public PrimitiveType(String str, Scope scope) {
        super(str);
        this.scope = scope;
    }

    @Override
    public void sameTypeOf(Type tp, Position pos) {
        if (getTypeName().equals(tp.getTypeName())) {

        } else {
            throw new SemanticError("Type error!", pos);
        }
    }

    @Override
    public boolean isArrayType() {
        return false;
    }

    @Override
    public boolean isPrimitiveType() {
        return true;
    }

    @Override
    public boolean isClassType() {
        return false;
    }

    @Override
    public boolean isNullType() {
        return false;
    }

    @Override
    public boolean isSameTypeOf(Type type) {
        return this == type;
    }
}
