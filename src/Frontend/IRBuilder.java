package Frontend;

import AST.*;
import Semantic.*;
import Utils.SemanticError;
import com.sun.source.tree.NewArrayTree;

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
                    segmentList.add(cs);
                    //constructor has no parameters
                    if (!y.isConstructor()) {
                        cs.setThisPointer(new VirtualRegister(cs, ((ClassDeclNode) x).getClassType()));
                        y.getParameterList().forEach(z -> {
                            VariableSymbol var = y.getFuncSymbol().getScope().findVarInScope(z.getIdentifier(), null);
                            var.setVirtualRegister(new VirtualRegister(cs, Type.getType(z.getType())));
                        });
                    } else {
                        ((ClassDeclNode) x).getClassType().setCreator(cs);
                    }
                });
            }
        });

        //create all code segments for all functions and prepare its parameters' virtual registers
        node.getDeclNodeList().forEach(x -> {
            if (x instanceof FuncDeclNode) {
                FunctionSymbol func = ((FuncDeclNode) x).getFunctionSymbol();
                CodeSegment cs = new CodeSegment(func);
                segmentList.add(cs);
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
                        currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, nv, y.getExpr().getVirtualRegister()));
                    }
                });
            }
        });

        segmentList.forEach(cs -> {
            if (cs.getFunctionSymbol().getType() == null) {
                currentSegment = cs;
                currentBlock = cs.getHeadBlock();
                CollectStmt(cs.getFunctionSymbol().getBlockContext().getStatementList(), null, null);
            } else {

            }
        });
    }
    void CollectStmt(StatementNode blockNode, BasicBlock continueBlock, BasicBlock breakBlock) {
        List<StatementNode> blockNodeList = new ArrayList<>();
        blockNodeList.add(blockNode);
        CollectStmt(blockNodeList, continueBlock, breakBlock);
    }

    void CollectStmt(List<StatementNode> blockNodeList, BasicBlock continueBlock, BasicBlock breakBlock) {
        for (StatementNode node : blockNodeList) {
                switch (node.getStmtType()) {
                    case BLOCK:
                        CollectStmt(node.getBlockStmtList(), continueBlock, breakBlock);
                        break;
                    case IF:
                        ComputExprValue(node.getIfExpr());
                        VirtualRegister ifExpr = node.getIfExpr().getVirtualRegister();
                        BasicBlock bb1 = currentBlock.split();
                        BasicBlock bb2 = bb1.split();
                        currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, ifExpr, false, bb2));
                        currentBlock = bb1;
                        CollectStmt(node.getIfStmt1(), continueBlock, breakBlock);
                        if (node.getIfStmt2() == null) {
                            currentBlock = bb2;
                        } else {
                            BasicBlock bb3 = bb2.split();
                            currentBlock.addInst(new JumpInstruction(IRInstruction.op.JUMP, bb3));
                            currentBlock = bb2;
                            CollectStmt(node.getIfStmt2(), continueBlock, breakBlock);
                            currentBlock = bb3;
                        }
                        break;
                    case FOR:
                        BasicBlock forCond = currentBlock.split();
                        BasicBlock forBody = forCond.split();
                        BasicBlock forUpdate = forBody.split();
                        BasicBlock forAfter = forUpdate.split();
                        ComputExprValue(node.getForControl().getInitialExpr());
                        currentBlock = forCond;
                        ComputExprValue(node.getForControl().getConditionExpr());
                        VirtualRegister fC = node.getForControl().getConditionExpr().getVirtualRegister();
                        currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, fC, false, forAfter));
                        currentBlock = forBody;
                        CollectStmt(node.getForStatement().getBlockStmtList(), forUpdate, forAfter);
                        currentBlock = forUpdate;
                        ComputExprValue(node.getForControl().getUpdateExpr());
                        currentBlock.addInst(new JumpInstruction(IRInstruction.op.JUMP, forCond));
                        currentBlock = forAfter;
                        break;
                    case WHILE:
                        BasicBlock whileCond = currentBlock.split();
                        BasicBlock whileBody = whileCond.split();
                        BasicBlock whileAfter = whileBody.split();
                        currentBlock = whileCond;
                        ComputExprValue(node.getWhileExpr());
                        VirtualRegister wC = node.getWhileExpr().getVirtualRegister();
                        currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, wC, false, whileAfter));
                        currentBlock = whileBody;
                        CollectStmt(node.getWhileStmt().getBlockStmtList(), whileCond, whileAfter);
                        currentBlock.addInst(new JumpInstruction(IRInstruction.op.JUMP, whileCond));
                        currentBlock = whileAfter;
                        break;
                    case RETURN:
                        if (node.getReturnExpr() != null) {
                            ComputExprValue(node.getReturnExpr());
                            VirtualRegister returnValue = node.getReturnExpr().getVirtualRegister();
                            currentBlock.addInst(new ReturnInstruction(IRInstruction.op.RETURN, returnValue));
                        } else {
                            currentBlock.addInst(new ReturnInstruction(IRInstruction.op.RETURN, null));
                        }
                    case BREAK:
                        currentBlock.addInst(new JumpInstruction(IRInstruction.op.JUMP, breakBlock));
                        break;
                    case CONTINUE:
                        currentBlock.addInst(new JumpInstruction(IRInstruction.op.JUMP, continueBlock));
                        break;
                    case EXPR:
                        ComputExprValue(node.getExpr());
                        break;
                    case VARDECL:
                        Type type = Type.getType(node.getVarDecl().getType());
                        node.getVarDecl().getVarDecoratorList().forEach(v -> {
                            VirtualRegister vn = new VirtualRegister(currentSegment, type);
                            v.getVariableSymbol().setVirtualRegister(vn);
                            if (v.getExpr() != null) {
                                ComputExprValue(v.getExpr());
                                VirtualRegister vv = v.getExpr().getVirtualRegister();
                                currentBlock.addInst(new SStoreInstruction(IRInstruction.op.SSTORE, vn.getAddr(), vv, type));
                            } else
                                currentBlock.addInst(new SStoreInstruction(IRInstruction.op.SSTORE, vn.getAddr(), null, type));
                        });
                        break;
                }
        }
    }

    public VirtualRegister NewArray(List<VirtualRegister> list, int dimension, int tmp, int listSize, Type tp) {
        if (tmp > listSize) {
            VirtualRegister rt = new VirtualRegister(currentSegment, tp);
            if (tp.isClassType()) {
                CodeSegment creator = ((ClassType) tp).getCreator();
                currentBlock.addInst(new CallInstruction(IRInstruction.op.CALL, rt, Scope.intType, creator, new ArrayList<>()));
            } else {
                currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, rt, 0));
            }
            return rt;
        }

        Type ctxType =  ((ArrayType) tp).getDimensionMinusOne();
        VirtualRegister len = list.get(tmp - 1);
        VirtualRegister malloc_size = new VirtualRegister(currentSegment, Scope.intType);
        VirtualRegister jump_size = new VirtualRegister(currentSegment, Scope.intType);
        currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, jump_size, ctxType.getWidth()));
        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, malloc_size, len, "*", jump_size));
        VirtualRegister rt = new VirtualRegister(currentSegment, Scope.intType);
        currentBlock.addInst(new MallocInstruction(IRInstruction.op.MALLOC, rt, malloc_size));
        VirtualRegister pointer = new VirtualRegister(currentSegment, Scope.intType);
        currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, pointer, rt));
        VirtualRegister endPointer = new VirtualRegister(currentSegment, Scope.intType);
        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, endPointer, pointer, "+", malloc_size));
        currentBlock = currentBlock.split();
        BasicBlock loopBlock = currentBlock;
        VirtualRegister ctx = NewArray(list, dimension, tmp + 1, listSize, ctxType);
        currentBlock.addInst(new StoreInstruction(IRInstruction.op.STORE, pointer, 0, ctx, ctxType));
        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, pointer, pointer, "+", jump_size));
        currentBlock.addInst(new BranchInstruction(IRInstruction.op.BRANCH, pointer, "<", endPointer, loopBlock));
        currentBlock = currentBlock.split();
        return rt;
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
                CreatorNode cr = node.getCreator();
                List<VirtualRegister> lis = new ArrayList<>();
                cr.getExpr().forEach(x -> {
                    ComputExprValue(x);
                    lis.add(x.getVirtualRegister());
                });
                if (cr.getExprType().isArrayType()) {
                    vn = NewArray(lis, cr.getDimension(), 1, lis.size(), cr.getExprType());
                } else {
                    //assert class type
                    CodeSegment creator = ((ClassType) cr.getExprType()).getCreator();
                    vn = new VirtualRegister(currentSegment, Scope.intType);
                    currentBlock.addInst(new CallInstruction(IRInstruction.op.CALL, vn, Scope.intType, creator, new ArrayList<>()));
                }
                node.setVirtualRegister(vn);
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
        VirtualRegister vn = null;
        switch (node.getType()) {
            case THIS:
                break;
            case LITERAL:
                switch (node.getLiteralNode().getLiteralType()) {
                    case STRING:
                        break;
                    case INT:
                        break;
                    case BOOL:
                        break;
                    case NULL:
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
                        currentBlock.addInst(new GAddInstruction(IRInstruction.op.GLOAD, vn, offset, var.getType()));
                        node.setVirtualRegister(vn);
                    } else {
                        currentBlock.addInst(new SAddInstruction(IRInstruction.op.SLOAD, vn, offset, var.getType()));
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
                    VirtualRegister offset = new VirtualRegister(currentSegment, Scope.intType);
                    currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, offset, vs.getOffsetInClass()));
                    currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, c, "+", offset));
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
                vn = off2;
                node.setVirtualRegister(vn);
                break;
            case CALL:
                break;
            case NEW:
                break;
            case POST:
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
                        node.setVirtualRegister(address);
                        break;
                    case "+":
                    case "-":
                    case "~":
                        break;
                    case "!":
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
                        break;
                    case "<=": case ">=": case "<": case ">":
                        break;
                    case "-": case "*": case "/": case "%": case "<<": case ">>":  case "&": case "^": case "|":
                        break;
                    case "&&": case "||":
                        break;
                    case "==": case "!=":
                        break;
                    case "=":
                        currentBlock.addInst(new StoreInstruction(IRInstruction.op.STORE, r1, 0, r2, node.getBinaryExpr1().getExprType()));
                        vn = r1;
                        break;
                }
                node.setVirtualRegister(vn);
                break;
        }
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
