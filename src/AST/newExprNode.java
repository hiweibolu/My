package AST;

import Util.position;
import java.util.ArrayList;

public class newExprNode extends ExprNode {
	
	public ArrayList<ExprNode> Exprs = new ArrayList<>();
	
    public newExprNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
