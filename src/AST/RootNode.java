package AST;

import Util.position;

import java.util.ArrayList;

public class RootNode extends ASTNode {
    public ArrayList<ASTNode> varDefs = new ArrayList<>();
    public ArrayList<ASTNode> funcDefs = new ArrayList<>();
    public ArrayList<ASTNode> classDefs = new ArrayList<>();

    public RootNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
