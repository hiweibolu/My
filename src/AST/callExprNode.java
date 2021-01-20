package AST;

import Util.position;
import java.util.ArrayList;

public class callExprNode extends ExprNode {
	
	public ArrayList<ExprNode> Params = new ArrayList<>();;

    public callExprNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
