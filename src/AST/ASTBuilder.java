package AST;

import Parser.MxBaseVisitor;
import Parser.MxParser;
import Utils.Position;
import Utils.SyntaxError;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;


import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {

    @Override
    public ASTNode visitProgram(MxParser.ProgramContext ctx) {
        List<ASTNode> list = new ArrayList<>();
        for (ParserRuleContext decl : ctx.declaration()) {
            list.add(visit(decl));
        }
        return new ProgramNode(new Position(ctx.getStart()), list);
    }

    @Override
    public ASTNode visitDeclaration(MxParser.DeclarationContext ctx) {
        if (ctx.classDeclaration() != null) return visit(ctx.classDeclaration());
        if (ctx.functionDeclaration() != null) return visit(ctx.functionDeclaration());
        if (ctx.variableDeclaration() != null) return visit(ctx.variableDeclaration());
        return null;
    }

    @Override
    public ASTNode visitClassDeclaration(MxParser.ClassDeclarationContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitClassBody(MxParser.ClassBodyContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitClassBodyDeclaration(MxParser.ClassBodyDeclarationContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitMethodDeclaration(MxParser.MethodDeclarationContext ctx) {
        return super.visitMethodDeclaration(ctx);
    }

    @Override
    public ASTNode visitVariableDeclaration(MxParser.VariableDeclarationContext ctx) {
        return super.visitVariableDeclaration(ctx);
    }

    @Override
    public ASTNode visitVariableDecorator(MxParser.VariableDecoratorContext ctx) {
        return super.visitVariableDecorator(ctx);
    }

    @Override
    public ASTNode visitType(MxParser.TypeContext ctx) {
        return super.visitType(ctx);
    }

    @Override
    public ASTNode visitTypeOrVoid(MxParser.TypeOrVoidContext ctx) {
        return super.visitTypeOrVoid(ctx);
    }

    @Override
    public ASTNode visitClassType(MxParser.ClassTypeContext ctx) {
        return super.visitClassType(ctx);
    }

    @Override
    public ASTNode visitPrimitiveType(MxParser.PrimitiveTypeContext ctx) {
        return super.visitPrimitiveType(ctx);
    }

    @Override
    public ASTNode visitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx) {
        return super.visitFunctionDeclaration(ctx);
    }

    @Override
    public ASTNode visitParameters(MxParser.ParametersContext ctx) {
        return super.visitParameters(ctx);
    }

    @Override
    public ASTNode visitParameterList(MxParser.ParameterListContext ctx) {
        return super.visitParameterList(ctx);
    }

    @Override
    public ASTNode visitParameter(MxParser.ParameterContext ctx) {
        return super.visitParameter(ctx);
    }

    @Override
    public ASTNode visitBlock(MxParser.BlockContext ctx) {
        return super.visitBlock(ctx);
    }

    @Override
    public ASTNode visitBlockStatement(MxParser.BlockStatementContext ctx) {
        return super.visitBlockStatement(ctx);
    }

    @Override
    public ASTNode visitBlockStmt(MxParser.BlockStmtContext ctx) {
        return super.visitBlockStmt(ctx);
    }

    @Override
    public ASTNode visitIfStmt(MxParser.IfStmtContext ctx) {
        return super.visitIfStmt(ctx);
    }

    @Override
    public ASTNode visitForStmt(MxParser.ForStmtContext ctx) {
        return super.visitForStmt(ctx);
    }

    @Override
    public ASTNode visitWhileStmt(MxParser.WhileStmtContext ctx) {
        return super.visitWhileStmt(ctx);
    }

    @Override
    public ASTNode visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        return super.visitReturnStmt(ctx);
    }

    @Override
    public ASTNode visitBreakStmt(MxParser.BreakStmtContext ctx) {
        return super.visitBreakStmt(ctx);
    }

    @Override
    public ASTNode visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        return super.visitContinueStmt(ctx);
    }

    @Override
    public ASTNode visitSemiStmt(MxParser.SemiStmtContext ctx) {
        return super.visitSemiStmt(ctx);
    }

    @Override
    public ASTNode visitExprStmt(MxParser.ExprStmtContext ctx) {
        return super.visitExprStmt(ctx);
    }

    @Override
    public ASTNode visitVariableDeclStmt(MxParser.VariableDeclStmtContext ctx) {
        return super.visitVariableDeclStmt(ctx);
    }

    @Override
    public ASTNode visitNewExpr(MxParser.NewExprContext ctx) {
        CreatorNode c = (CreatorNode) visit(ctx.creator());
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.NEW, c);
    }//done

    @Override
    public ASTNode visitPrefixExpr(MxParser.PrefixExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.PRE,
                ctx.prefix.getText(), (ExpressionNode) visit(ctx.expression()));
    }//done

    @Override
    public ASTNode visitMethodCallExpr(MxParser.MethodCallExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.CALL,
                (ExpressionNode) visit(ctx.expression()),
                ((ExpressionListNode) visit(ctx.expressionList())).getList());
    }//done

    @Override
    public ASTNode visitPrimaryExpr(MxParser.PrimaryExprContext ctx) {
        return visit(ctx.primary());
    }//done

    @Override
    public ASTNode visitArrayExpr(MxParser.ArrayExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.ARRAY,
                (ExpressionNode) visit(ctx.expression(0)),
                (ExpressionNode) visit(ctx.expression(1)));
    }//done

    @Override
    public ASTNode visitMemberExpr(MxParser.MemberExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.MEMBER,
                  (ExpressionNode) visit(ctx.expression()),ctx.op.getText(), ctx.Identifier().getText());
    }//done

    @Override
    public ASTNode visitBinaryOpExpr(MxParser.BinaryOpExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.BINARY,
                (ExpressionNode) visit(ctx.expression(0)), ctx.op.getText(),
                (ExpressionNode) visit(ctx.expression(1)));
    }//done

    @Override
    public ASTNode visitPostfixExpr(MxParser.PostfixExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.POST,
                (ExpressionNode) visit(ctx.expression()), ctx.postfix.getText());
    }//done

    @Override
    public ASTNode visitExpressionList(MxParser.ExpressionListContext ctx) {
        List<ExpressionNode> list = new ArrayList<>();
        for (ParserRuleContext expr : ctx.expression()) {
            list.add((ExpressionNode) visit(expr));
        }
        return new ExpressionListNode(new Position(ctx.getStart()), list);
    }//done

    @Override
    public ASTNode visitErrorCreator(MxParser.ErrorCreatorContext ctx) {
        try {
            throw new SyntaxError("Invalid new expression", new Position(ctx.getStart()));
        } catch (SyntaxError syntaxError) {
            syntaxError.printStackTrace();
        }
        return null;
    }//done

    @Override
    public ASTNode visitArrayCreator(MxParser.ArrayCreatorContext ctx) {
        List<ExpressionNode> e = new ArrayList<>();
        for (ParserRuleContext expr : ctx.expression())
            e.add((ExpressionNode) visit(expr));
        int d = 0; String str = ctx.getText();
        for (int i = 0; i < str.length(); ++i)
            if (str.charAt(i) == '[') ++d;
        if (ctx.classType() != null)
            return new CreatorNode(new Position(ctx.getStart()), ctx.classType().getText(), false, e, d);
        if (ctx.primitiveType() != null)
            return new CreatorNode(new Position(ctx.getStart()), ctx.primitiveType().getText(), true, e, d);
        return null;
    }//done

    @Override
    public ASTNode visitConstructorCreator(MxParser.ConstructorCreatorContext ctx) {
        if (ctx.classType() != null)
            return new CreatorNode(new Position(ctx.getStart()), ctx.classType().getText(), false, null, 0);
        if (ctx.primitiveType() != null)
            return new CreatorNode(new Position(ctx.getStart()), ctx.primitiveType().getText(), true, null, 0);
        return null;
    }//done

    @Override
    public ASTNode visitParenthesizedExpr(MxParser.ParenthesizedExprContext ctx) {
        return visit(ctx.expression());
    }//done

    @Override
    public ASTNode visitThisExpr(MxParser.ThisExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.THIS);
    }//done

    @Override
    public ASTNode visitLiteralExpr(MxParser.LiteralExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.LITERAL,
                (LiteralNode) visit(ctx.literal()));
    }//done

    @Override
    public ASTNode visitNameExpr(MxParser.NameExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.IDENTIFIER,
                ctx.Identifier().getText());
    }//done

    @Override
    public ASTNode visitLiteral(MxParser.LiteralContext ctx) {
        if (ctx.IntegerLiteral() != null)
            return new LiteralNode(new Position(ctx.getStart()),
                    LiteralNode.literalType.INT,
                    null,
                    Integer.valueOf(ctx.IntegerLiteral().getText()));
        if (ctx.StringLiteral() != null)
            return new LiteralNode(new Position(ctx.getStart()),
                    LiteralNode.literalType.STRING,
                    ctx.StringLiteral().getText(),
                    0);
        if (ctx.BooleanLiteral() != null)
            return new LiteralNode(new Position(ctx.getStart()),
                    LiteralNode.literalType.BOOL,
                    ctx.BooleanLiteral().getText(),
                    0);
        if (ctx.NullLiteral() != null)
            return new LiteralNode(new Position(ctx.getStart()),
                    LiteralNode.literalType.NULL,
                    ctx.NullLiteral().getText(),
                    0);
        return null;
    }//done

    @Override
    public ASTNode visitForControl(MxParser.ForControlContext ctx) {
        return super.visitForControl(ctx);
    }
}
