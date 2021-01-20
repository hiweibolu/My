package AST;

public interface ASTVisitor {
    void visit(RootNode it);
    void visit(funcDefNode it);
    void visit(funcParamNode it);
    void visit(classDefNode it);
	
    void visit(blockStmtNode it);
    void visit(breakStmtNode it);
    void visit(continueStmtNode it);
    void visit(exprStmtNode it);
    void visit(forStmtNode it);
    void visit(ifStmtNode it);
    void visit(returnStmtNode it);
    void visit(varDefStmtNode it);
    void visit(varListStmtNode it);
    void visit(whileStmtNode it);
	
    void visit(assignExprNode it);
    void visit(binaryExprNode it);
    void visit(callExprNode it);
    void visit(indexExprNode it);
    void visit(literalExprNode it);
    void visit(memberExprNode it);
    void visit(newExprNode it);
    void visit(prefixExprNode it);
    void visit(suffixExprNode it);
    void visit(thisExprNode it);
    void visit(varExprNode it);
}
