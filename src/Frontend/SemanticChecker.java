package Frontend;

import AST.*;
import Util.Scope;
import Util.Type;
import Util.error.semanticError;
import Util.globalScope;

import java.util.HashMap;
import java.util.ArrayList;

public class SemanticChecker implements ASTVisitor {
    private Scope currentScope;
    private globalScope gScope;
    private Type returnType = null, currentClass = null;
	private int loopDepth = 0;
	private boolean haveReturn = false, inFunction = false, notInClass = true;
	private HashMap<String, Scope> classesScope;

    public SemanticChecker(globalScope gScope) {
        currentScope = this.gScope = gScope;
    }

    @Override
    public void visit(RootNode it) {
		gScope.addType("int", new Scope(gScope), it.pos);
		gScope.addType("bool", new Scope(gScope), it.pos);
		gScope.addType("void", new Scope(gScope), it.pos);
		gScope.addType("string", new Scope(gScope), it.pos);
        it.Defs.forEach(d -> d.accept(this));
		if (!gScope.containsFunction("main", false))
			throw new semanticError("main() function not found. ", it.pos);
    }
	
	@Override
    public void visit(funcDefNode it) {
		currentScope.defineFunction(it.name, it.type.getFuncType(), it.pos);
		ArrayList<Type> params = new ArrayList<>();
		it.funcParams.forEach(p -> params.add(p.type));
		currentScope.defineFunction(it.name, it.type, it.pos);
		currentScope.defineParams(it.name, params, it.pos);
		
        currentScope = new Scope(currentScope);
		it.funcParams.forEach(p -> p.accept(this));
		returnType = it.type;
		haveReturn = false;
		inFunction = true;
        it.block.accept(this);
		inFunction = false;
		if (!haveReturn){
			if (it.name != "main") throw new semanticError("Function has no return. ", it.pos);
		}

		currentScope = currentScope.parentScope();
    }
	
	@Override
    public void visit(funcParamNode it) {
		currentScope.defineVariable(it.name, it.type, it.pos);
    }

    @Override
    public void visit(classDefNode it) {
		currentScope = new Scope(currentScope);
		currentClass = new Type(it.name);
		
        it.varDefs.forEach(vd -> vd.accept(this));
        it.funcDefs.forEach(fd -> fd.accept(this));
		
		currentClass = null;
		gScope.addType(it.name, currentScope, it.pos);
		currentScope = gScope;
    }

    @Override
    public void visit(varDefStmtNode it) {
        /*if (currentStruct != null) {
            assert (currentStruct.members != null);
            if (currentStruct.members.containsKey(it.name))
                throw new semanticError("redefinition of member " + it.name, it.pos);
            currentStruct.members.put(it.name, gScope.getTypeFromName(it.typeName, it.pos));
            if (it.init != null)
                throw new semanticError("Yx does not support default init of members",
                            it.init.pos);
        }*/
		
		if (!gScope.containsType(it.type.name)) 
			throw new semanticError("Vardef types havent defined. ", it.pos);
		
        if (it.init != null) {
            it.init.accept(this);
            if (!it.init.type.equals(it.type))
                throw new semanticError("Vardef types not match. ", it.init.pos);
        }
        currentScope.defineVariable(it.name, it.type, it.pos);
    }

    @Override
    public void visit(returnStmtNode it) {
		if (!inFunction)
			throw new semanticError("Cant return coz it is not in a function. ", it.pos);
		haveReturn = true;
        if (it.value != null) {
            it.value.accept(this);
			if (!returnType.equals(it.value.type))
				throw new semanticError("Function return type unmatch. ", it.pos);
        }else{
			if (!returnType.isVoid())
				throw new semanticError("Function return type unmatch. ", it.pos);
		}
    }

    @Override
    public void visit(blockStmtNode it) {
        if (!it.stmts.isEmpty()) {
            currentScope = new Scope(currentScope);
            for (StmtNode stmt : it.stmts) stmt.accept(this);
            currentScope = currentScope.parentScope();
        }
    }

    @Override
    public void visit(exprStmtNode it) {
        it.expr.accept(this);
    }
	
	@Override
    public void visit(breakStmtNode it) {
        if (loopDepth <= 0)
			throw new semanticError("Cant break coz it is not in a loop. ", it.pos);
    }
	
	@Override
    public void visit(continueStmtNode it) {
        if (loopDepth <= 0)
			throw new semanticError("Cant continue coz it is not in a loop. ", it.pos);
    }

	@Override
    public void visit(whileStmtNode it) {
        if (it.condition != null){
			it.condition.accept(this);
			if (!it.condition.type.isBool())
				throw new semanticError("While condition should be a bool",
						it.condition.pos);
		}
		loopDepth++;
		it.Stmt.accept(this);
		loopDepth--;
    }

    @Override
    public void visit(ifStmtNode it) {
        it.condition.accept(this);
        if (!it.condition.type.isBool())
            throw new semanticError("If condition should be a bool",
                    it.condition.pos);
        it.thenStmt.accept(this);
        if (it.elseStmt != null) it.elseStmt.accept(this);
    }
	
    @Override
    public void visit(forStmtNode it) {
        if (it.condition != null){
			it.condition.accept(this);
			if (!it.condition.type.isBool())
				throw new semanticError("For condition should be a bool",
						it.condition.pos);
		}
        if (it.init != null) it.init.accept(this);
        if (it.incr != null) it.incr.accept(this);
		
		loopDepth++;
		it.Stmt.accept(this);
		loopDepth--;
    }

    @Override
    public void visit(callExprNode it) {
		it.Params.forEach(p -> p.accept(this));
		ExprNode func = it.Params.get(0);
		if (!func.type.isFunc)
			throw new semanticError("callFunc : it is not a function. ", it.pos);
		ArrayList<Type> tList = currentScope.getParams(func.type.name, true);
		if (it.Params.size() - 1 != tList.size())
			throw new semanticError("callFunc : params numbers unmatch. ", it.pos);
		for (int i = 0; i < tList.size(); i++)
			if (!it.Params.get(i + 1).type.equals(tList.get(i)))
				throw new semanticError("callFunc : params types unmatch. ", it.pos);
		it.type = new Type(it.Params.get(0).type);
    }

    @Override
    public void visit(assignExprNode it) {
        it.rhs.accept(this);
        it.lhs.accept(this);
        if (!it.rhs.type.equals(it.lhs.type))
            throw new semanticError("Assign type not match. ", it.pos);
        /*if (!it.lhs.isAssignable())
            throw new semanticError("Semantic Error: not assignable", it.lhs.pos);*/
        it.type = new Type(it.rhs.type);
    }
	
	@Override
    public void visit(suffixExprNode it) {
        it.hs.accept(this);
		if (!it.hs.type.isInt()) throw new semanticError("suffixExpr: should be an int. ", it.pos);
		it.type = new Type("int");
    }
	
    @Override
    public void visit(prefixExprNode it) {
        it.hs.accept(this);
		switch (it.opCode){
			case add:
			case sub:
			case addadd:
			case subsub:
			case no:
				if (!it.hs.type.isInt()) throw new semanticError("prefixExpr: should be an int. ", it.pos);
				it.type = new Type("int");
				break;
			case not:
				if (!it.hs.type.isBool()) throw new semanticError("prefixExpr: should be a bool. ", it.pos);
				it.type = new Type("bool");
		}
    }

    @Override
    public void visit(binaryExprNode it) {
        it.lhs.accept(this);
        it.rhs.accept(this);
		switch (it.opCode){
			case eq:
			case neq:
				if (!it.lhs.type.equals(it.rhs.type)){
					if (it.lhs.type.dimension > 0 && it.rhs.type.isNull());
					else throw new semanticError("binaryExpr: types should match. ", it.pos);
				}
				it.type = new Type("bool");
				break;
			case ge:
			case geq:
			case le:
			case leq:
				if (it.lhs.type.isInt() && it.rhs.type.isInt() 
					|| it.lhs.type.isString() && it.rhs.type.isString()) it.type = new Type("bool");
				else throw new semanticError("binaryExpr: should be an int or a string. ", it.pos);
				break;
			case add:
				if (it.lhs.type.isInt() && it.rhs.type.isInt()) it.type = new Type("int");
				else if (it.lhs.type.isString() && it.rhs.type.isString()) it.type = new Type("string");
				else throw new semanticError("binaryExpr: should be an int or a string. ", it.pos);
				break;
			case oror:
			case andand:
				if (!it.lhs.type.isBool() || !it.rhs.type.isBool())
					throw new semanticError("binaryExpr: should be a bool. ", it.pos);
				it.type = new Type("bool");
				break;
			case sub:
			case or:
			case and:
			case xor:
			case shl:
			case shr:
			case mul:
			case div:
			case mod:
				if (!it.lhs.type.isInt() || !it.rhs.type.isInt())
					throw new semanticError("binaryExpr: should be an int. ", it.pos);
				it.type = new Type("int");
		}
    }
	
	@Override
    public void visit(indexExprNode it) {
		it.lhs.accept(this);
		it.rhs.accept(this);
		if (!it.rhs.type.isInt())
			throw new semanticError("when use [], index should be an int. ", it.rhs.pos);
		if (it.lhs.type.dimension <= 0) 
			throw new semanticError("when use [], dimension should be positive. ", it.lhs.pos);
		it.type = new Type(it.lhs.type);
		it.type.dimension--;
	}
	
	@Override
    public void visit(memberExprNode it) {
		it.hs.accept(this);
		Scope tempScope = currentScope;
		currentScope = gScope.getScopeFromName(it.hs.type.name, it.pos);
		notInClass = false;
		
		it.member.accept(this);
		notInClass = true;
		
		currentScope = tempScope;
		it.type = it.member.type;
	}

	@Override
    public void visit(newExprNode it) {
		for (ExprNode e : it.Exprs){
			e.accept(this);
			if (!e.type.isInt())
				throw new semanticError("when new, index should be an int. ", it.pos);
		}
	}

    @Override
    public void visit(literalExprNode it) {}

	@Override
    public void visit(thisExprNode it) {
        if (currentClass == null)
			throw new semanticError("this should be in a class. ", it.pos);
		it.type = currentClass;
    }

    @Override
    public void visit(varExprNode it) {
		if (currentScope.containsFunction(it.name, notInClass)){
			it.type = currentScope.getTypeFunction(it.name, notInClass);
		}
		else{
			if (!currentScope.containsVariable(it.name, notInClass))
				throw new semanticError("Variable not defined. ", it.pos);
			it.type = currentScope.getTypeVariable(it.name, notInClass);
		}
    }
}
