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
    private BasicBlock currentBlock;
    private VirtualRegister sp;
    private VirtualRegister startsp;
    private VirtualRegister ap;
    private List<CopyInstruction> constPoolWriteBack;
    private CodeSegment stringAdd;
    private CodeSegment stringCmp;

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

    private void fullfillStringAdd() {
        /*
        currentSegment = stringAdd;
        currentBlock = stringAdd.getHeadBlock();
        //load 2 address
        VirtualRegister r1 = new VirtualRegister(currentSegment, Scope.intType);
        VirtualRegister r2 = new VirtualRegister(currentSegment, Scope.intType);
        currentBlock.addInst(new SLoadInstruction(IRInstruction.op.SLOAD, r1, 0, Scope.intType));
        currentBlock.addInst(new SLoadInstruction(IRInstruction.op.SLOAD, r2, 4, Scope.intType));
        //load 2 lengths
        VirtualRegister len1 = new VirtualRegister(currentSegment, Scope.intType);
        VirtualRegister len2 = new VirtualRegister(currentSegment, Scope.intType);
        VirtualRegister len = new VirtualRegister(currentSegment, Scope.intType);
        currentBlock.addInst(new LoadInstruction(IRInstruction.op.LOAD, len1, r1, 0, Scope.intType));
        currentBlock.addInst(new LoadInstruction(IRInstruction.op.LOAD, len2, r2, 0, Scope.intType));
        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, len, len1, "+", len2));
        //length plus 4
        VirtualRegister len_plus_4 = new VirtualRegister(currentSegment, Scope.intType);
        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, len_plus_4, len, "+", 4));
        VirtualRegister tmp = new VirtualRegister(currentSegment, Scope.intType);
        //malloc len + 4
        currentBlock.addInst(new MallocInstruction(IRInstruction.op.MALLOC, tmp, len_plus_4));
        //write len
        currentBlock.addInst(new StoreInstruction(IRInstruction.op.STORE, tmp, 0, len, Scope.intType));
        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, tmp, tmp, "+", 4));
        BasicBlock bb = currentBlock.split();
        */
    }

    private void fullfillStringCmp() {

    }

    @Override
    public void visit(ProgramNode node) {
        constantPoolTable = new ConstantPoolTable();
        //get Class size
        Map<String, Type> typeMap = globalScope.getTypeMap();
        typeMap.forEach((name, type) -> {
            type.setWidth();
        });

        stringAdd = new CodeSegment(null);
        stringCmp = new CodeSegment(null);

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
        currentBlock = globalVarSegment.getHeadBlock();
        node.getDeclNodeList().forEach(x -> {
            if (x instanceof VarDeclNode) {
                Type type = Type.getType(((VarDeclNode) x).getType());
                ((VarDeclNode) x).getVarDecoratorList().forEach(y -> {
                    if (y.getExpr() == null) {
                        VirtualRegister nv = new VirtualRegister(globalVarSegment, type);
                        y.getVariableSymbol().setVirtualRegister(nv);
                    } else {
                        ComputExprValue(y.getExpr());
                        VirtualRegister nv = new VirtualRegister(globalVarSegment, type);
                        y.getVariableSymbol().setVirtualRegister(nv);
                        //TODO : check copy instruction
                        globalVarSegment.getHeadBlock().addInst(new CopyInstruction(IRInstruction.op.COPY, nv, y.getExpr().getVirtualRegister()));
                    }
                });
            }
        });
        sp = new VirtualRegister(globalVarSegment, Scope.intType);
        ap = new VirtualRegister(globalVarSegment, Scope.intType);


    }

    public void ComputExprValue(ExpressionNode node) {
        VirtualRegister vn = null;
        switch (node.getType()) {
            case THIS:
                vn = new VirtualRegister(currentSegment, Scope.intType);
                VirtualRegister th = currentSegment.getThisPointer();
                currentBlock.addInst(new SLoadInstruction(IRInstruction.op.SLOAD, vn, th.getAddr(), Scope.intType));
                node.setVirtualRegister(vn);
                break;
            case LITERAL:
                switch (node.getLiteralNode().getLiteralType()) {
                    case STRING:
                        int s = constantPoolTable.getAddress();
                        constantPoolTable.allocate(node.getLiteralNode().getStr());
                        vn = new VirtualRegister(currentSegment, Scope.stringType);
                        CopyInstruction ci = new CopyInstruction(IRInstruction.op.COPY, vn, s);
                        currentBlock.addInst(ci);
                        constPoolWriteBack.add(ci);
                        node.setVirtualRegister(vn);
                        break;
                    case INT:
                        int num = node.getLiteralNode().getNum();
                        vn = new VirtualRegister(currentSegment, Scope.intType);
                        currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, num));
                        node.setVirtualRegister(vn);
                        break;
                    case BOOL:
                        boolean b = node.getLiteralNode().getStr().equals("true");
                        vn = new VirtualRegister(currentSegment, Scope.boolType);
                        currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, b ? 1 : 0));
                        node.setVirtualRegister(vn);
                        break;
                    case NULL:
                        vn = new VirtualRegister(currentSegment, Scope.nullType);
                        currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 0));
                        node.setVirtualRegister(vn);
                        break;
                }
                break;
            case IDENTIFIER:
                if (!node.isFunction()) {
                    assert node.isLeftValue();
                    VariableSymbol var = node.getScope().findVar(node.getIdentifier(), node.getPosition());
                    VirtualRegister varReg = var.getVirtualRegister();
                    Address offset = varReg.getAddr();
                    vn = new VirtualRegister(currentSegment, var.getType());
                    if (varReg.getInCodeSegment() == globalVarSegment) {
                        currentBlock.addInst(new GLoadInstruction(IRInstruction.op.GLOAD, vn, offset, var.getType()));
                        node.setVirtualRegister(vn);
                    } else {
                        currentBlock.addInst(new SLoadInstruction(IRInstruction.op.SLOAD, vn, offset, var.getType()));
                        node.setVirtualRegister(vn);
                    }
                } else {
                    FunctionSymbol func = node.getScope().findFunc(node.getIdentifier(), node.getPosition());
                    node.setFuncPointer(func.getCodeSegment());
                }
                break;
            case MEMBER:
                ComputExprValue(node.getMemberExpr());
                VirtualRegister c = node.getMemberExpr().getVirtualRegister();
                Type t = node.getMemberExpr().getExprType();
                Scope classScope;
                if (t instanceof ClassType) classScope = ((ClassType) t).getScope();
                else if (t instanceof ArrayType) classScope = ArrayType.getScope();
                else classScope = ((PrimitiveType) t).getScope();

                if (node.isFunction()) {
                    FunctionSymbol fs = classScope.findFuncInScope(node.getIdentifier(), node.getPosition());
                    node.setFuncPointer(fs.getCodeSegment());
                    node.setVirtualRegister(c);
                } else {
                    VariableSymbol vs = classScope.findVarInScope(node.getIdentifier(), node.getPosition());
                    vn = new VirtualRegister(currentSegment, vs.getType());
                    currentBlock.addInst(new LoadInstruction(IRInstruction.op.LOAD, vn, c, vs.getOffsetInClass(), vs.getType()));
                    node.setVirtualRegister(vn);
                }
                break;
            case ARRAY:
                ComputExprValue(node.getArrayExprBefore());
                ComputExprValue(node.getArrayExprAfter());
                VirtualRegister bef = node.getArrayExprBefore().getVirtualRegister();
                VirtualRegister aft = node.getArrayExprAfter().getVirtualRegister();
                int a = node.getExprType().getWidth();
                VirtualRegister off = new VirtualRegister(currentSegment, Scope.intType);
                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, off, aft, "*", a));
                VirtualRegister off2 = new VirtualRegister(currentSegment, Scope.intType);
                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, off2, bef, "+", off));
                vn = new VirtualRegister(currentSegment, node.getExprType());
                currentBlock.addInst(new LoadInstruction(IRInstruction.op.LOAD, vn, off2, 0, node.getExprType()));
                node.setVirtualRegister(vn);
                break;
            case CALL:
                FunctionSymbol func = node.getCallExpr().getFuncSymbol();
                List<VirtualRegister> list = new ArrayList<>();
                if (func.getScope() != globalScope)
                    list.add(node.getCallExpr().getVirtualRegister());
                for (ExpressionNode ex : node.getCallExprList()) {
                    ComputExprValue(ex);
                    list.add(ex.getVirtualRegister());
                }
                vn = new VirtualRegister(currentSegment, node.getExprType());
                currentBlock.addInst(new CallInstruction(IRInstruction.op.CALL, vn, node.getExprType(), func.getCodeSegment(), list));
                node.setVirtualRegister(vn);
                break;
            case NEW:
                //TODO: gp
                node.getCreator().setScope(node.getScope());
                node.getCreator().accept(this);
                node.setExprType(node.getCreator().getExprType());
                break;
            case POST:
                ComputExprAddr(node.getPostExpr());
                VirtualRegister addr = node.getPostExpr().getVirtualRegister();
                vn = new VirtualRegister(currentSegment, Scope.intType);
                currentBlock.addInst(new LoadInstruction(IRInstruction.op.LOAD, vn, addr, 0, Scope.intType));
                VirtualRegister res = new VirtualRegister(currentSegment, Scope.intType);
                switch (node.getOp()) {
                    case "++":
                        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, res, vn, "+", 1));
                        break;
                    case "--":
                        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, res, vn, "-", 1));
                        break;
                }
                currentBlock.addInst(new StoreInstruction(IRInstruction.op.STORE, addr, 0, res, Scope.intType));
                node.setVirtualRegister(vn);
                break;
            case PRE:
                switch (node.getOp()) {
                    case "++":
                    case "--":
                        ComputExprAddr(node.getPreExpr());
                        VirtualRegister address = node.getPreExpr().getVirtualRegister();
                        VirtualRegister num = new VirtualRegister(currentSegment, Scope.intType);
                        currentBlock.addInst(new LoadInstruction(IRInstruction.op.LOAD, num, address, 0, Scope.intType));
                        vn = new VirtualRegister(currentSegment, Scope.intType);
                        if (node.getOp().equals("++"))
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, num, "+", 1));
                        else
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, num, "-", 1));
                        currentBlock.addInst(new StoreInstruction(IRInstruction.op.STORE, address, 0, vn, Scope.intType));
                        node.setVirtualRegister(vn);
                        break;
                    case "+":
                    case "-":
                    case "~":
                        ComputExprValue(node.getPreExpr());
                        VirtualRegister num2 = node.getPreExpr().getVirtualRegister();
                        if (node.getOp().equals("+"))
                            node.setVirtualRegister(num2);
                        else if (node.getOp().equals("-")) {
                            vn = new VirtualRegister(currentSegment, Scope.intType);
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, null, "-", num2));
                            node.setVirtualRegister(vn);
                        } else {
                            vn = new VirtualRegister(currentSegment, Scope.intType);
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, num2, "^", -1));
                            node.setVirtualRegister(vn);
                        }
                        break;
                    case "!":
                        ComputExprValue(node.getPreExpr());
                        VirtualRegister bool_res = node.getPreExpr().getVirtualRegister();
                        vn = new VirtualRegister(currentSegment, Scope.boolType);
                        BasicBlock bb1 = currentBlock.split();
                        BasicBlock bb2 = bb1.split();
                        BasicBlock bb3 = bb2.split();
                        currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, bool_res, true, bb2));
                        bb1.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 1));
                        bb1.addInst(new JumpInstruction(IRInstruction.op.JUMP, bb3));
                        bb2.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 0));//NOTICE: not SSA!!
                        currentBlock = bb3;
                        node.setVirtualRegister(vn);
                        break;
                }
                break;
            case BINARY:
                VirtualRegister r1 = null, r2 = null;
                if (!node.getOp().equals("&&") && !node.getOp().equals("||")) {
                    if (node.getOp().equals("=")) {
                        ComputExprAddr(node.getBinaryExpr1());
                        ComputExprValue(node.getBinaryExpr2());
                        r1 = node.getBinaryExpr1().getVirtualRegister();
                        r2 = node.getBinaryExpr2().getVirtualRegister();
                    } else {
                        ComputExprValue(node.getBinaryExpr1());
                        ComputExprValue(node.getBinaryExpr2());
                        r1 = node.getBinaryExpr1().getVirtualRegister();
                        r2 = node.getBinaryExpr2().getVirtualRegister();
                    }
                }
                switch (node.getOp()) {
                    case "+":
                        if (Scope.intType.isSameTypeOf(node.getBinaryExpr1().getExprType())) {
                            vn = new VirtualRegister(currentSegment, Scope.intType);
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, r1, "+", r2));
                        } else {
                            vn = new VirtualRegister(currentSegment, Scope.intType);
                            List<VirtualRegister> li = new ArrayList<>();
                            li.add(r1); li.add(r2);
                            currentBlock.addInst(new CallInstruction(IRInstruction.op.CALL, vn, Scope.intType, stringAdd, li));
                        }
                        break;
                    case "<=": case ">=": case "<": case ">":
                        if (Scope.intType.isSameTypeOf(node.getBinaryExpr1().getExprType())) {
                            vn = new VirtualRegister(currentSegment, Scope.boolType);
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, r1, node.getOp(), r2));
                        } else {
                            VirtualRegister result = new VirtualRegister(currentSegment, Scope.intType);
                            vn = new VirtualRegister(currentSegment, Scope.boolType);
                            List<VirtualRegister> li = new ArrayList<>();
                            li.add(r1); li.add(r2);
                            currentBlock.addInst(new CallInstruction(IRInstruction.op.CALL, result, Scope.intType, stringCmp, li));
                            if (node.getOp().equals("<=")) {
                                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, result, "<=", 0));
                            } else if (node.getOp().equals(">=")) {
                                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, result, ">=", 0));
                            } else if (node.getOp().equals("<")) {
                                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, result, "<", 0));
                            } else {
                                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, result, ">", 0));
                            }
                        }
                        break;
                    case "-": case "*": case "/": case "%": case "<<": case ">>":  case "&": case "^": case "|":
                        vn = new VirtualRegister(currentSegment, Scope.intType);
                        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, r1, node.getOp(), r2));
                        break;
                    case "&&": case "||":
                        ComputExprValue(node.getBinaryExpr1());
                        r1 = node.getBinaryExpr1().getVirtualRegister();
                        BasicBlock bb1 = currentBlock.split();
                        BasicBlock bb2 = bb1.split();
                        BasicBlock bb3 = bb2.split();
                        BasicBlock bb4 = bb3.split();
                        if (node.getOp().equals("&&")) {
                            currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, r1, false, bb3));
                            currentBlock = bb1;
                            ComputExprValue(node.getBinaryExpr2());
                            r2 = node.getBinaryExpr2().getVirtualRegister();
                            currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, r2, false, bb3));
                            vn = new VirtualRegister(currentSegment, Scope.boolType);
                            bb2.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 1));
                            bb2.addInst(new JumpInstruction(IRInstruction.op.JUMP, bb4));
                            bb3.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 0));
                            currentBlock = bb4;//not SSA!
                        } else {
                            currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, r1, true, bb3));
                            currentBlock = bb1;
                            ComputExprValue(node.getBinaryExpr2());
                            r2 = node.getBinaryExpr2().getVirtualRegister();
                            currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, r2, true, bb3));
                            vn = new VirtualRegister(currentSegment, Scope.boolType);
                            bb2.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 0));
                            bb2.addInst(new JumpInstruction(IRInstruction.op.JUMP, bb4));
                            bb3.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 1));
                            currentBlock = bb4;//not SSA!
                        }
                        break;
                    case "==": case "!=":
                        if (Scope.stringType.isSameTypeOf(node.getBinaryExpr1().getExprType())) {
                            VirtualRegister result = new VirtualRegister(currentSegment, Scope.intType);
                            vn = new VirtualRegister(currentSegment, Scope.boolType);
                            List<VirtualRegister> li = new ArrayList<>();
                            li.add(r1); li.add(r2);
                            currentBlock.addInst(new CallInstruction(IRInstruction.op.CALL, result, Scope.intType, stringCmp, li));
                            if (node.getOp().equals("==")) {
                                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, result, "==", 0));
                            } else {
                                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, result, "!=", 0));
                            }
                        } else {
                            vn = new VirtualRegister(currentSegment, Scope.boolType);
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, r1, node.getOp(), r2));
                        }
                        break;
                    case "=":
                        currentBlock.addInst(new StoreInstruction(IRInstruction.op.STORE, r1, 0, r2, node.getBinaryExpr1().getExprType()));
                        vn = r2;
                        break;
                }
                node.setVirtualRegister(vn);
                break;
        }
    }

    public void ComputExprAddr(ExpressionNode node) {

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
