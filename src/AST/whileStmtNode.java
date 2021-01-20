package AST;

import Util.position;

public class whileStmtNode extends StmtNode {
    public ExprNode condition;
    public StmtNode Stmt;

    public whileStmtNode(ExprNode condition, StmtNode Stmt, position pos) {
        super(pos);
        this.condition = condition;
        this.Stmt = Stmt;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
