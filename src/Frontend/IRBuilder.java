package Frontend;

import AST.*;
import Semantic.*;
import Utils.SemanticError;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class IRBuilder extends ASTVisitor {
    private CodeSegment globalVarSegment;
    private ConstantPoolTable constantPoolTable;
    private List<CodeSegment> segmentList;
    private CodeSegment currentSegment;
    private VirtualRegister sp;
    private VirtualRegister ap;
    private List<CopyInstruction> constPoolWriteBack;

    //things to write back:
    //const pool pointer
    //global variable address
    //function pointer

    private List<Type> typeList;

    public IRBuilder(Scope globalScope) {
        super(globalScope);
        segmentList = new ArrayList<>();
        constPoolWriteBack = new ArrayList<>();
    }

    @Override
    public void visit(ProgramNode node) {
        constantPoolTable = new ConstantPoolTable();
        //get Class size
        Map<String, Type> typeMap = globalScope.getTypeMap();
        typeMap.forEach((name, type) -> {
            type.setWidth();
        });

        //set class member's offset and
        //create all code segments and its parameters' virtual registers for all class methods
        node.getDeclNodeList().forEach(x -> {
            if (x instanceof ClassDeclNode) {
                //set class member's offset
                AtomicInteger offset = new AtomicInteger();
                ((ClassDeclNode) x).getVarDeclNodeList().forEach(y -> {
                    Type type = Type.getType(y.getType());
                    y.getVarDecoratorList().forEach(z -> {
                        z.getVariableSymbol().setOffsetInClass(offset.get());
                        offset.addAndGet(type.getWidth());
                    });
                });
                assert offset.equals(((ClassDeclNode) x).getClassType().getWidth());
                //create all code segments and its parameters' virtual registers for all class methods
                ((ClassDeclNode) x).getMethodDeclNodeList().forEach(y -> {
                    CodeSegment cs = new CodeSegment(y.getFuncSymbol());
                    //constructor has no parameters
                    if (!y.isConstructor()) {
                        cs.setThisPointer(new VirtualRegister(cs, ((ClassDeclNode) x).getClassType()));
                        y.getParameterList().forEach(z -> {
                            VariableSymbol var = y.getFuncSymbol().getScope().findVarInScope(z.getIdentifier(), null);
                            var.setVirtualRegister(new VirtualRegister(cs, Type.getType(z.getType())));
                        });
                    }
                });
            }
        });

        //create all code segments for all functions and prepare its parameters' virtual registers
        node.getDeclNodeList().forEach(x -> {
            if (x instanceof FuncDeclNode) {
                FunctionSymbol func = ((FuncDeclNode) x).getFunctionSymbol();
                CodeSegment cs = new CodeSegment(func);
                ((FuncDeclNode) x).getParameterList().forEach(y -> {
                    VariableSymbol var = func.getScope().findVarInScope(y.getIdentifier(), null);
                    var.setVirtualRegister(new VirtualRegister(cs, Type.getType(y.getType())));
                });
            }
        });

        //scan global variables
        globalVarSegment = new CodeSegment(null);
        currentSegment = globalVarSegment;
        node.getDeclNodeList().forEach(x -> {
            if (x instanceof VarDeclNode) {
                Type type = Type.getType(((VarDeclNode) x).getType());
                ((VarDeclNode) x).getVarDecoratorList().forEach(y -> {
                    if (y.getExpr() == null) {
                        VirtualRegister nv = new VirtualRegister(globalVarSegment, type);
                        y.getVariableSymbol().setVirtualRegister(nv);
                    } else {
                        y.getExpr().accept(this);
                        VirtualRegister nv = new VirtualRegister(globalVarSegment, type);
                        y.getVariableSymbol().setVirtualRegister(nv);
                        globalVarSegment.addIRInst(new CopyInstruction(IRInstruction.op.COPY, nv, y.getExpr().getVirtualRegister()));
                    }
                });
            }
        });
        sp = new VirtualRegister(globalVarSegment, Scope.intType);
        ap = new VirtualRegister(globalVarSegment, Scope.intType);


    }

    @Override
    public void visit(BlockNode node) {

    }

    @Override
    public void visit(ClassBodyNode node) {

    }

    @Override
    public void visit(ClassDeclNode node) {

    }

    @Override
    public void visit(ClassTypeNode node) {

    }

    @Override
    public void visit(CreatorNode node) {

    }

    @Override
    public void visit(ExpressionListNode node) {

    }

    @Override
    public void visit(ExpressionNode node) {
        VirtualRegister vn;
        switch (node.getType()) {
            case THIS:
                node.setVirtualRegister(currentSegment.getThisPointer());
                break;
            case LITERAL:
                switch (node.getLiteralNode().getLiteralType()) {
                    case STRING:
                        int s = constantPoolTable.getAddress();
                        constantPoolTable.allocate(node.getLiteralNode().getStr());
                        vn = new VirtualRegister(currentSegment, Scope.stringType);
                        CopyInstruction ci = new CopyInstruction(IRInstruction.op.COPY, vn, s);
                        currentSegment.addIRInst(ci);
                        constPoolWriteBack.add(ci);
                        node.setVirtualRegister(vn);
                        break;
                    case INT:
                        int num = node.getLiteralNode().getNum();
                        vn = new VirtualRegister(currentSegment, Scope.intType);
                        currentSegment.addIRInst(new CopyInstruction(IRInstruction.op.COPY, vn, num));
                        node.setVirtualRegister(vn);
                        break;
                    case BOOL:
                        boolean b = node.getLiteralNode().getStr().equals("true");
                        vn = new VirtualRegister(currentSegment, Scope.boolType);
                        currentSegment.addIRInst(new CopyInstruction(IRInstruction.op.COPY, vn, b ? 1 : 0));
                        node.setVirtualRegister(vn);
                        break;
                    case NULL:
                        vn = new VirtualRegister(currentSegment, Scope.nullType);
                        currentSegment.addIRInst(new CopyInstruction(IRInstruction.op.COPY, vn, 0));
                        node.setVirtualRegister(vn);
                        break;
                }
                break;
            case IDENTIFIER:
                if (!node.isFunction()) {
                    assert node.isLeftValue();
                    VariableSymbol var = node.getScope().findVar(node.getIdentifier(), node.getPosition());
                    VirtualRegister varReg = var.getVirtualRegister();
                    int offset = varReg.getRelativeAddress();
                    VirtualRegister addr = new VirtualRegister(currentSegment, Scope.intType);
                    if (varReg.getInCodeSegment() == globalVarSegment) {
                        currentSegment.addIRInst(new CopyInstruction(IRInstruction.op.COPY, addr, varReg.getAddr()));
                        node.setVirtualRegister(addr);
                    } else {
                        VirtualRegister offsetReg = new VirtualRegister(currentSegment, Scope.intType);
                        currentSegment.addIRInst(new CopyInstruction(IRInstruction.op.COPY, offsetReg, offset));
                        currentSegment.addIRInst(new BinaryInstruction(IRInstruction.op.BINARY, addr, sp, "+", offsetReg));
                        node.setVirtualRegister(var.getVirtualRegister());
                    }
                } else {
                    FunctionSymbol func = node.getScope().findFunc(node.getIdentifier(), node.getPosition());
                    node.setFuncPointer(func.getCodeSegment());
                }
                break;
            case MEMBER:
                node.getMemberExpr().accept(this);
                Type c = node.getMemberExpr().getExprType();
                Scope classScope;
                if (c instanceof ClassType) classScope = ((ClassType) c).getScope();
                else if (c instanceof ArrayType) classScope = ArrayType.getScope();
                else classScope = ((PrimitiveType) c).getScope();

                VirtualRegister a = node.getMemberExpr().getVirtualRegister();
                if (node.isFunction()) {
                    FunctionSymbol fs = classScope.findFuncInScope(node.getIdentifier(), node.getPosition());
                } else {
                    VariableSymbol vs = classScope.findVarInScope(node.getIdentifier(), node.getPosition());
                    vs.getOffsetInClass()
                }

                try {
                    VariableSymbol vs = classScope.findVarInScope(node.getIdentifier(), node.getPosition());
                    node.setExprType(vs.getType());
                    node.setIsLeftValue(true);
                } catch (SemanticError e) {
                    FunctionSymbol fs = classScope.findFuncInScope(node.getIdentifier(), node.getPosition());
                    node.setExprType(null);
                    node.setFunction(fs);
                }
                break;
            case ARRAY:
                node.getArrayExprBefore().setScope(node.getScope());
                node.getArrayExprBefore().accept(this);
                Type type = node.getArrayExprBefore().getExprType();
                Type art = null;
                if (type instanceof ArrayType) {
                    if (((ArrayType) type).getDimension() > 1)
                        art = new ArrayType(type.getTypeName(),
                                ((ArrayType) type).getType(), ((ArrayType) type).getDimension() - 1);
                    else
                        art = ((ArrayType) type).getType();
                } else
                    throw new SemanticError("Must be array type!", node.getPosition());
                node.getArrayExprAfter().setScope(node.getScope());
                node.getArrayExprAfter().accept(this);
                assert Scope.intType.isSameTypeOf(node.getArrayExprAfter().getExprType());
                node.setExprType(art);
                node.setIsLeftValue(true);
                break;
            case CALL:
                node.getCallExpr().setScope(node.getScope());
                node.getCallExpr().accept(this);
                assert node.getCallExpr().isFunction();
                FunctionSymbol func = node.getCallExpr().getFuncSymbol();
                List<Type> list1 = func.getParameters();
                Iterator<Type> it = list1.iterator();
                assert list1.size() == node.getCallExprList().size();
                for (ExpressionNode ex : node.getCallExprList()) {
                    ex.setScope(node.getScope());
                    ex.accept(this);
                    Type rec = it.next();
                    assert rec.isSameTypeOf(ex.getExprType())
                            || ((rec instanceof ArrayType || rec instanceof ClassType)
                            && ex.getExprType() == Scope.nullType);
                }
                node.setExprType(func.getType());
                break;
            case NEW:
                node.getCreator().setScope(node.getScope());
                node.getCreator().accept(this);
                node.setExprType(node.getCreator().getExprType());
                break;
            case POST:
                node.getPostExpr().setScope(node.getScope());
                node.getPostExpr().accept(this);
                assert Scope.intType.isSameTypeOf(node.getPostExpr().getExprType());
                assert node.getPreExpr().isLeftValue();
                node.setExprType(Scope.intType);
                switch (node.getOp()) {
                    case "++":
                        break;
                    case "--":
                        break;
                }
                break;
            case PRE:
                node.getPreExpr().setScope(node.getScope());
                node.getPreExpr().accept(this);
                switch (node.getOp()) {
                    case "++":
                    case "--":
                        assert node.getPreExpr().isLeftValue();
                        assert Scope.intType.isSameTypeOf(node.getPostExpr().getExprType());
                        node.setExprType(Scope.intType);
                        node.setIsLeftValue(true);
                        break;
                    case "+":
                    case "-":
                    case "~":
                        assert Scope.intType.isSameTypeOf(node.getPostExpr().getExprType());
                        node.setExprType(Scope.intType);
                        break;
                    case "!":
                        assert Scope.boolType.isSameTypeOf(node.getPostExpr().getExprType());
                        node.setExprType(Scope.boolType);
                        break;
                }
                break;
            case BINARY:
                node.getBinaryExpr1().setScope(node.getScope());
                node.getBinaryExpr1().accept(this);
                node.getBinaryExpr2().setScope(node.getScope());
                node.getBinaryExpr2().accept(this);
                switch (node.getOp()) {
                    case "+":
                        assert node.getBinaryExpr1().getExprType().isSameTypeOf(node.getBinaryExpr2().getExprType());
                        assert Scope.intType.isSameTypeOf(node.getBinaryExpr1().getExprType())
                                || Scope.stringType.isSameTypeOf(node.getBinaryExpr1().getExprType());
                        node.setExprType(node.getBinaryExpr1().getExprType());
                        break;
                    case "<=": case ">=": case "<": case ">":
                        assert node.getBinaryExpr1().getExprType().isSameTypeOf(node.getBinaryExpr2().getExprType());
                        assert Scope.intType.isSameTypeOf(node.getBinaryExpr1().getExprType())
                                || Scope.stringType.isSameTypeOf(node.getBinaryExpr1().getExprType());
                        node.setExprType(Scope.boolType);
                        break;
                    case "-": case "*": case "/": case "%": case "<<": case ">>":  case "&": case "^": case "|":
                        assert Scope.intType.isSameTypeOf(node.getBinaryExpr1().getExprType());
                        assert Scope.intType.isSameTypeOf(node.getBinaryExpr2().getExprType());
                        node.setExprType(Scope.intType);
                        break;
                    case "&&": case "||":
                        assert Scope.boolType.isSameTypeOf(node.getBinaryExpr1().getExprType());
                        assert Scope.boolType.isSameTypeOf(node.getBinaryExpr2().getExprType());
                        node.setExprType(Scope.boolType);
                        break;
                    case "==": case "!=":
                        if (node.getBinaryExpr1().getExprType() instanceof ArrayType) {
                            assert node.getBinaryExpr2().getExprType() == Scope.nullType
                                    || node.getBinaryExpr1().getExprType().isSameTypeOf(node.getBinaryExpr2().getExprType());
                        } else if (node.getBinaryExpr1().getExprType() instanceof ClassType) {
                            assert node.getBinaryExpr2().getExprType() == Scope.nullType
                                    || node.getBinaryExpr1().getExprType().isSameTypeOf(node.getBinaryExpr2().getExprType());
                        } else if (node.getBinaryExpr1().getExprType() == Scope.nullType) {
                            assert node.getBinaryExpr2().getExprType() == Scope.nullType
                                    || node.getBinaryExpr2().getExprType() instanceof ArrayType
                                    || node.getBinaryExpr2().getExprType() instanceof ClassType;
                        } else {
                            assert Scope.intType.isSameTypeOf(node.getBinaryExpr1().getExprType())
                                    || Scope.boolType.isSameTypeOf(node.getBinaryExpr1().getExprType())
                                    || Scope.stringType.isSameTypeOf(node.getBinaryExpr1().getExprType());
                            assert node.getBinaryExpr1().getExprType().isSameTypeOf(node.getBinaryExpr2().getExprType());
                        }
                        node.setExprType(Scope.boolType);
                        break;
                    case "=":
                        assert node.getBinaryExpr1().isLeftValue();
                        if (Scope.intType.isSameTypeOf(node.getBinaryExpr1().getExprType())) {
                            assert Scope.intType.isSameTypeOf(node.getBinaryExpr2().getExprType());
                        } else if (Scope.boolType.isSameTypeOf(node.getBinaryExpr1().getExprType())) {
                            assert Scope.boolType.isSameTypeOf(node.getBinaryExpr2().getExprType());
                        } else if (Scope.stringType.isSameTypeOf(node.getBinaryExpr1().getExprType())) {
                            assert Scope.stringType.isSameTypeOf(node.getBinaryExpr2().getExprType());
                        } else {
                            assert node.getBinaryExpr1().getExprType().isSameTypeOf(node.getBinaryExpr2().getExprType())
                                    || Scope.nullType.isSameTypeOf(node.getBinaryExpr2().getExprType());
                        }
                        node.setExprType(node.getBinaryExpr1().getExprType());
                        node.setIsLeftValue(node.getBinaryExpr1().isLeftValue());
                        break;
                }
                break;
        }
    }

    @Override
    public void visit(ForControlNode node) {

    }

    @Override
    public void visit(FuncDeclNode node) {

    }

    @Override
    public void visit(LiteralNode node) {

    }

    @Override//Remember to set this-pointer
    public void visit(MethodDeclNode node) {

    }

    @Override
    public void visit(ParameterNode node) {

    }

    @Override
    public void visit(ParametersNode node) {

    }

    @Override
    public void visit(PrimitiveTypeNode node) {

    }

    @Override
    public void visit(StatementNode node) {

    }

    @Override
    public void visit(TypeNode node) {

    }

    @Override
    public void visit(TypeOrVoidNode node) {

    }

    @Override
    public void visit(VarDeclNode node) {

    }

    @Override
    public void visit(VarDecoratorNode node) {

    }
}
