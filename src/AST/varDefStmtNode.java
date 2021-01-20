package AST;

import Util.position;
import Util.Type;

public class varDefStmtNode extends StmtNode {
    public String name;
	public Type type;
    public ExprNode init;

    public varDefStmtNode(Type type, String name, ExprNode init, position pos) {
        super(pos);
        this.name = name;
        this.type = type;
        this.init = init;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
