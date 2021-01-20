package AST;

import Util.Type;
import Util.position;

public abstract class ExprNode extends ASTNode {
    public Type type, parent;
	public String funcName;

    public ExprNode(position pos) {
        super(pos);
		parent = null;
    }

    public boolean isAssignable() {
        return false;
    }
}
