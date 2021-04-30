package AST;

import Util.position;
import Util.Scope;
import IR.IRRegIdentifier;

abstract public class ASTNode {
    public position pos;

    public IRRegIdentifier regId, pRegId;
    public Scope scope;
    public int literal = 0;
	public boolean inClass = false;
	public Integer and_short = null, or_short = null;
	public boolean shorted = false;

    public ASTNode(position pos) {
        this.pos = pos;
        this.scope = null;
    }

    abstract public void accept(ASTVisitor visitor);
}
