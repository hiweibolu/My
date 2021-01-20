package AST;

import Util.position;
import Util.Type;

public class funcParamNode extends ASTNode {
    public String name;
	public Type type;

    public funcParamNode(Type type, String name, position pos) {
        super(pos);
        this.name = name;
        this.type = type;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
