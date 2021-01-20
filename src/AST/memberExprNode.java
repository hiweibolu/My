package AST;

import Util.Type;
import Util.position;

public class memberExprNode extends ExprNode {
    public ExprNode hs;
	public varExprNode member;

    public memberExprNode(ExprNode hs, varExprNode member, position pos) {
        super(pos);
        this.hs = hs;
        this.member = member;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
