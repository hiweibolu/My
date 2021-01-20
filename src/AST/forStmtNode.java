package AST;

import Util.position;

public class forStmtNode extends StmtNode {
    public ExprNode condition, init, incr;
    public StmtNode Stmt;

    public forStmtNode(ExprNode condition, ExprNode init, ExprNode incr, StmtNode Stmt, position pos) {
        super(pos);
        this.condition = condition;
        this.init = init;
        this.incr = incr;
        this.Stmt = Stmt;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
