package AST;

import Util.Type;
import Util.position;

public class suffixExprNode extends ExprNode {
    public ExprNode hs;
    public enum suffixOpType {
        add, sub, addadd, subsub, not, no
    }
    public suffixOpType opCode;

    public suffixExprNode(ExprNode hs, suffixOpType opCode, position pos) {
        super(pos);
        this.hs = hs;
        this.opCode = opCode;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
