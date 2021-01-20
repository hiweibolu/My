package AST;

import Util.Type;
import Util.position;

public class prefixExprNode extends ExprNode {
    public ExprNode hs;
    public enum prefixOpType {
        add, sub, addadd, subsub, not, no
    }
    public prefixOpType opCode;

    public prefixExprNode(ExprNode hs, prefixOpType opCode, position pos) {
        super(pos);
        this.hs = hs;
        this.opCode = opCode;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
