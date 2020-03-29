package Semantic;

import AST.*;
import Utils.AccessError;
import Utils.SemanticError;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SemanticCheckVisitor extends ASTVisitor {
    private boolean sameType(Type a, Type b) {
        if (a == null && b == null) return true;
        if (a != null) return a.isSameTypeOf(b);
        else return false;
    }

    public SemanticCheckVisitor(Scope globalScope) {
        super(globalScope);
    }

    @Override
    public void visit(ProgramNode node) {
        node.getDeclNodeList().forEach(x -> {
            x.accept(this);
        });
    }//done

    @Override
    public void visit(BlockNode node) {
        node.getStatementList().forEach(st -> {
            st.setScope(node.getScope());
            st.accept(this);
        });
    }//done

    @Override
    public void visit(ClassBodyNode node) {
        throw new AccessError("Invalid Access!", node.getPosition());
    }//done

    @Override
    public void visit(ClassDeclNode node) {
        Scope currentScope = node.getClassType().getScope();
        node.getVarDeclNodeList().forEach(x -> {
            Type type = Type.getType(x.getType());
            x.getVarDecoratorList().forEach(y -> {
                y.setVariableSymbol(new VariableSymbol(type, y.getIdentifier(), y, y.getPosition()));
                currentScope.addVariable(y.getVariableSymbol());
            });
        });
        node.getMethodDeclNodeList().forEach(x -> {
            Type returnType;
            if (x.isConstructor()) returnType = null;
            else returnType = Type.getReturnType(x.getTypOrVoid());
            x.setFuncSymbol(new FunctionSymbol(returnType, x.getIdentifier(), x, currentScope, x.getParameterList()));
            currentScope.addFunction(x.getFuncSymbol());
        });
        node.getMethodDeclNodeList().forEach(x -> {
            x.getBlock().setScope(x.getFuncSymbol().getScope());
            x.getBlock().accept(this);
        });
    }//done

    @Override
    public void visit(ClassTypeNode node) {
        throw new AccessError("Invalid Access!", node.getPosition());
    }//done

    @Override
    public void visit(CreatorNode node) {
        node.getExpr().forEach(ex -> {
            ex.setScope(node.getScope());
            ex.accept(this);
            assert Scope.intType.isSameTypeOf(ex.getExprType());
        });
        Type type = globalScope.findType(node.getId(), node.getPosition());
        if (node.getDimension() > 0) {
            node.setExprType(new ArrayType(node.getId(), type, node.getDimension()));
        } else {
            node.setExprType(type);
        }
    }//done

    @Override
    public void visit(ExpressionListNode node) {
        throw new AccessError("Invalid Access!", node.getPosition());
    }//done

    @Override
    public void visit(ExpressionNode node) {
        switch (node.getType()) {
            case THIS:
                node.setExprType(node.getScope().getClassType());
                break;
            case LITERAL:
                switch (node.getLiteralNode().getLiteralType()) {
                    case STRING:
                        node.setExprType(Scope.stringType);
                        break;
                    case INT:
                        node.setExprType(Scope.intType);
                        break;
                    case BOOL:
                        node.setExprType(Scope.boolType);
                        break;
                    case NULL:
                        node.setExprType(Scope.nullType);
                        break;
                }
                break;
            case IDENTIFIER:
                VariableSymbol var = node.getScope().findVar(node.getIdentifier(), node.getPosition());
                node.setExprType(var.getType());
                node.setIsLeftValue(true);
                break;
            case MEMBER:
                node.getMemberExpr().setScope(node.getScope());
                node.getMemberExpr().accept(this);
                Type c = node.getMemberExpr().getExprType();
                assert c instanceof ClassType | c == Scope.stringType;
                Scope classScope;
                if (c instanceof ClassType) classScope = ((ClassType) c).getScope();
                else classScope = ((PrimitiveType) c).getScope();
                try {
                    VariableSymbol vs = classScope.findVarInScope(node.getIdentifier(), node.getPosition());
                    node.setExprType(vs.getType());
                    node.setIsLeftValue(true);
                } catch (SemanticError e) {
                    FunctionSymbol fs = classScope.findFuncInScope(node.getIdentifier(), node.getPosition());
                    node.setExprType(fs.getType());
                    node.setFunction(fs);
                }
                break;
            case ARRAY:
                node.getArrayExprBefore().setScope(node.getScope());
                node.getArrayExprBefore().accept(this);
                Type type = node.getArrayExprBefore().getExprType();
                ArrayType art = null;
                if (type instanceof ArrayType)
                    art = new ArrayType(type.getTypeName(),
                        ((ArrayType) type).getType(), ((ArrayType) type).getDimension() + 1);
                else
                    art = new ArrayType(type.getTypeName(), type, 1);
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
                    assert it.next().isSameTypeOf(ex.getExprType());
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
                assert Scope.intType.isSameTypeOf(node.getPostExpr().getExprType());
                node.setExprType(Scope.intType);
                switch (node.getOp()) {
                    case "+":
                        break;
                    case "-":
                        break;
                    case "++":
                        break;
                    case "--":
                        break;
                }
                break;
            case BINARY:
                node.getBinaryExpr1().setScope(node.getScope());
                node.getBinaryExpr1().accept(this);
                node.getBinaryExpr2().setScope(node.getScope());
                node.getBinaryExpr2().accept(this);
                switch (node.getOp()) {
                    case "+": case "<=": case ">=": case "<": case ">":
                        assert node.getBinaryExpr1().getExprType().isSameTypeOf(node.getBinaryExpr2().getExprType());
                        assert Scope.intType.isSameTypeOf(node.getBinaryExpr1().getExprType())
                                || Scope.stringType.isSameTypeOf(node.getBinaryExpr1().getExprType());
                        node.setExprType(node.getBinaryExpr1().getExprType());
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
                        assert node.getBinaryExpr1().getExprType().isSameTypeOf(node.getBinaryExpr2().getExprType());
                        assert Scope.intType.isSameTypeOf(node.getBinaryExpr1().getExprType())
                                || Scope.boolType.isSameTypeOf(node.getBinaryExpr1().getExprType())
                                || Scope.stringType.isSameTypeOf(node.getBinaryExpr1().getExprType());
                        node.setExprType(node.getBinaryExpr1().getExprType());
                        break;
                    case "=":
                        if (Scope.intType.isSameTypeOf(node.getBinaryExpr1().getExprType())) {
                            assert Scope.intType.isSameTypeOf(node.getBinaryExpr2().getExprType());
                        } else if (Scope.boolType.isSameTypeOf(node.getBinaryExpr1().getExprType())) {
                            assert Scope.boolType.isSameTypeOf(node.getBinaryExpr2().getExprType());
                        } else if (Scope.stringType.isSameTypeOf(node.getBinaryExpr1().getExprType())) {
                            assert Scope.stringType.isSameTypeOf(node.getBinaryExpr2().getExprType());
                        } else if (node.getBinaryExpr1().getExprType() instanceof ArrayType) {
                            assert node.getBinaryExpr1().getExprType().isSameTypeOf(node.getBinaryExpr2().getExprType())
                                    || Scope.nullType.isSameTypeOf(node.getBinaryExpr2().getExprType());
                        } else {
                            throw new SemanticError("Type not found!", node.getPosition());
                        }
                        node.setExprType(node.getBinaryExpr1().getExprType());
                        node.setIsLeftValue(node.getBinaryExpr1().isLeftValue());
                        break;
                }
                break;
        }
    }//done

    @Override
    public void visit(ForControlNode node) {
        node.getInitialExpr().setScope(node.getScope());
        node.getInitialExpr().accept(this);
        node.getConditionExpr().setScope(node.getScope());
        node.getConditionExpr().accept(this);
        assert Scope.boolType.isSameTypeOf(node.getConditionExpr().getExprType());
        node.getUpdateExpr().setScope(node.getScope());
        node.getUpdateExpr().accept(this);
    }//done

    @Override
    public void visit(FuncDeclNode node) {
        FunctionSymbol nowFunc = globalScope.findFunc(node.getIdentifier(), node.getPosition());
        node.getBlock().setScope(nowFunc.getScope());
        node.getBlock().accept(this);
    }//done

    @Override
    public void visit(LiteralNode node) {

    }//done

    @Override
    public void visit(MethodDeclNode node) {
        throw new AccessError("Invalid Access!", node.getPosition());
    }//done

    @Override
    public void visit(ParameterNode node) {
        throw new AccessError("Invalid Access!", node.getPosition());
    }//done

    @Override
    public void visit(ParametersNode node) {
        throw new AccessError("Invalid Access!", node.getPosition());
    }//done

    @Override
    public void visit(PrimitiveTypeNode node) {
        throw new AccessError("Invalid Access!", node.getPosition());
    }//done

    @Override
    public void visit(StatementNode node) {
        switch (node.getStmtType()) {
            case BLOCK:
                node.setScope(new Scope(node.getScope()));
                node.getBlockStmtList().forEach(st -> {
                    st.setScope(node.getScope());
                    st.accept(this);
                });
                break;
            case IF:
                node.getIfExpr().setScope(node.getScope());
                node.getIfExpr().accept(this);
                assert Scope.boolType.isSameTypeOf(node.getIfExpr().getExprType());
                node.getIfStmt1().setScope(new Scope(node.getScope()));
                node.getIfStmt1().accept(this);
                node.getIfStmt2().setScope(new Scope(node.getScope()));
                node.getIfStmt2().accept(this);
                break;
            case FOR:
                node.getForControl().setScope(node.getScope());
                node.getForControl().accept(this);
                node.getForStatement().setScope(new Scope(node.getScope(), true));
                node.getForStatement().accept(this);
                break;
            case WHILE:
                node.getWhileExpr().setScope(node.getScope());
                node.getWhileExpr().accept(this);
                assert Scope.boolType.isSameTypeOf(node.getWhileExpr().getExprType());
                node.getWhileStmt().setScope(new Scope(node.getScope(), true));
                node.getWhileStmt().accept(this);
                break;
            case RETURN:
                if (node.getReturnExpr() != null) {
                    node.getReturnExpr().accept(this);
                    assert node.getReturnExpr().getExprType().isSameTypeOf(node.getScope().getReturnType());
                } else {
                    assert node.getScope().getReturnType() == null;
                }
                break;
            case BREAK:
            case CONTINUE:
                assert node.getScope().isInLoop();
                break;
            case EXPR:
                node.getExpr().setScope(node.getScope());
                node.getExpr().accept(this);
                break;
            case VARDECL:
                node.getVarDecl().setScope(node.getScope());
                node.getVarDecl().accept(this);
                break;
        }
    }//done

    @Override
    public void visit(TypeNode node) {
        throw new AccessError("Invalid Access!", node.getPosition());
    }//done

    @Override
    public void visit(TypeOrVoidNode node) {
        throw new AccessError("Invalid Access!", node.getPosition());
    }//done

    @Override
    public void visit(VarDeclNode node) {
        Type type = Type.getType(node.getType());
        node.getVarDecoratorList().forEach(dec -> {
            node.getScope().addVariable(new VariableSymbol(type, dec.getIdentifier(), dec, dec.getPosition()));
            dec.getExpr().setScope(node.getScope());
            dec.getExpr().accept(this);
            assert type.isSameTypeOf(dec.getExpr().getExprType());
        });
    }//done

    @Override
    public void visit(VarDecoratorNode node) {
        throw new AccessError("Invalid Access!", node.getPosition());
    }//done
}
