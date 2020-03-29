package Semantic;

import Utils.Position;
import Utils.SemanticError;

public class ArrayType extends Type {
    private Type type;
    private int dimension;

    public ArrayType(String str, Type tp, int di) {
        super(str);
        this.type = tp;
        this.dimension = di;
    }

    public Type getType() {
        return type;
    }

    public int getDimension() {
        return dimension;
    }

    public void dimensionPlusOne() {
        dimension++;
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
        return true;
    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }

    @Override
    public boolean isStringType() {
        return false;
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
        if (type.isArrayType()) {
            return this.type == type && this.dimension == ((ArrayType) type).getDimension();
        } else {
            return false;
        }
    }
}
