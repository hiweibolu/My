package AST;

import Util.position;
import Util.Type;
import Parser.MxParser;

public class literalExprNode extends ExprNode {

    public MxParser.LiteralExprContext ctx;

    public literalExprNode(Type type, position pos, MxParser.LiteralExprContext ctx) {
        super(pos);
        this.type = type;
        this.ctx = ctx;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
