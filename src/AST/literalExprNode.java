package AST;

import Util.position;
import Util.Type;

public class literalExprNode extends ExprNode {

    public literalExprNode(Type type, position pos) {
        super(pos);
        this.type = type;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
