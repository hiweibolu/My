package AST;

import Util.position;
import Util.Type;

import java.util.ArrayList;

public class funcDefNode extends ASTNode {
    public ArrayList<funcParamNode> funcParams = new ArrayList<>();
    public String name;
	public Type type;
	public blockStmtNode block;

    public funcDefNode(position pos, String name, Type type, blockStmtNode block) {
        super(pos);
        this.name = name;
        this.type = type;
		this.block = block;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
