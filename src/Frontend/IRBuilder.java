package Frontend;

import AST.*;
import Semantic.Scope;
import Semantic.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IRBuilder extends ASTVisitor {
    private CodeSegment globalVarSegment;
    private List<CodeSegment> segmentList;

    private List<Type> typeList;

    public IRBuilder(Scope globalScope) {
        super(globalScope);
        segmentList = new ArrayList<>();
    }

    @Override
    public void visit(ProgramNode node) {
        //get Class size
        Map<String, Type> typeMap = globalScope.getTypeMap();
        typeMap.forEach((name, type) -> {
            type.setWidth();
        });

        globalVarSegment = new CodeSegment(null);
        node.getDeclNodeList().forEach(x -> {
            if (x instanceof VarDeclNode) {

            }
        });
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

    @Override
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
