package AST;

import Utils.Position;

import java.util.List;

/*
expression
    : primary                                    #primaryExpr
    | expression op='.' Identifier               #memberExpr
    | expression '[' expression ']'              #arrayExpr
    | expression '(' expressionList? ')'         #methodCallExpr
    | NEW creator                                #newExpr
    | expression postfix=('++' | '--')           #postfixExpr
    | prefix=('+'|'-'|'++'|'--') expression      #prefixExpr
    | prefix=('~'|'!') expression                #prefixExpr
    | expression op=('*'|'/'|'%') expression     #binaryOpExpr
    | expression op=('+'|'-') expression         #binaryOpExpr
    | expression op=('<<' | '>>>' | '>>') expression #binaryOpExpr
    | expression op=('<=' | '>=' | '>' | '<') expression #binaryOpExpr
    | expression op=('==' | '!=') expression     #binaryOpExpr
    | expression op='&' expression               #binaryOpExpr
    | expression op='^' expression               #binaryOpExpr
    | expression op='|' expression               #binaryOpExpr
    | expression op='&&' expression              #binaryOpExpr
    | expression op='||' expression              #binaryOpExpr
    | <assoc=right> expression op='=' expression #binaryOpExpr
    ;

primary
    : '(' expression ')' #parenthesizedExpr
    | THIS               #thisExpr
    | literal            #literalExpr
    | Identifier         #nameExpr
    ;
 */

public class ExpressionNode extends ASTNode {
    private String id, op;
    private Type type;
    private LiteralNode literal;
    private ExpressionNode expr1, expr2;
    private List<ExpressionNode> exprList;
    private CreatorNode creator;

    public ExpressionNode(Position pos, Type tp, String id) {
        super(pos);
        this.type = tp;
        assert tp == Type.IDENTIFIER;
        this.id = id;
    }

    public String getIdentifier() {
        return id;
    }

    public String getOp() {
        return op;
    }

    public Type getType() {
        return type;
    }

    public LiteralNode getLiteralNode() {
        return literal;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode e1, String op, ExpressionNode e2) {
        super(pos);
        this.type = tp;
        assert tp == Type.BINARY;
        this.expr1 = e1;
        this.op = op;
        this.expr2 = e2;
    }

    public ExpressionNode getBinaryExpr1() {
        return expr1;
    }

    public ExpressionNode getBinaryExpr2() {
        return expr2;
    }

    public ExpressionNode(Position pos, Type tp, String op, ExpressionNode e1) {
        super(pos);
        this.type = tp;
        assert tp == Type.PRE;
        this.op = op;
        this.expr1 = e1;
    }

    public ExpressionNode getPreExpr() {
        return expr1;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode e1, String op) {
        super(pos);
        this.type = tp;
        assert tp == Type.POST;
        this.expr1 = e1;
        this.op = op;
    }

    public ExpressionNode getPostExpr() {
        return expr1;
    }

    public ExpressionNode(Position pos, Type tp, LiteralNode li) {
        super(pos);
        this.type = tp;
        assert tp == Type.LITERAL;
        this.literal = li;
    }

    public ExpressionNode(Position pos, Type tp) {
        super(pos);
        this.type = tp;
        assert tp == Type.THIS;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode ex, String op, String id) {
        super(pos);
        this.type = tp;
        assert tp == Type.MEMBER && op.equals('.');
        this.expr1 = ex;
        this.op = op;
        this.id = id;
    }

    public ExpressionNode getMemberExpr() {
        return expr1;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode ex1, ExpressionNode ex2) {
        super(pos);
        this.type = tp;
        assert tp == Type.ARRAY;
        this.expr1 = ex1;
        this.expr2 = ex2;
    }

    public ExpressionNode getArrayExprBefore() {
        return expr1;
    }

    public ExpressionNode getArrayExprAfter() {
        return expr2;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode ex1, List<ExpressionNode> list) {
        super(pos);
        this.type = tp;
        assert tp == Type.CALL;
        this.expr1 = ex1;
        this.exprList = list;
    }

    public ExpressionNode getCallExpr() {
        return expr1;
    }

    public List<ExpressionNode> getCallExprList() {
        return exprList;
    }

    public ExpressionNode(Position pos, Type tp, CreatorNode cr) {
        super(pos);
        this.type = tp;
        assert tp == Type.NEW;
        this.creator = cr;
    }

    public CreatorNode getCreator() {
        return creator;
    }

    @Override
    public void accept(ASTVisitor visitor) {

    }

    enum Type {THIS, LITERAL, IDENTIFIER, MEMBER, ARRAY, CALL, NEW, POST, PRE, BINARY,}
}
