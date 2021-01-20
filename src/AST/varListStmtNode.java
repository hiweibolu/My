package AST;

import Util.position;
import Util.Type;

import java.util.ArrayList;

public class varListStmtNode extends StmtNode {
	
	public ArrayList<varDefStmtNode> stmts = new ArrayList<>();


    public varListStmtNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
