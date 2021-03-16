package Frontend;

import AST.*;
import Util.Scope;
import Util.Type;
import Util.RegIdAllocator;
import Util.error.semanticError;
import Util.globalScope;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import IR.*;

public class SemanticChecker implements ASTVisitor {
    private Scope currentScope;
    private globalScope gScope;
    private Type returnType = null, currentClass = null;
	private int loopDepth = 0;
	private boolean haveReturn = false, inFunction = false, notInClass = true;
	private HashMap<String, Scope> classesScope;
	private boolean inInit = false, inMainInit = false, inVarInit = false, constructDefined = false;

    private IRBlockList gBList;
    public SemanticChecker(IRBlockList gBList, globalScope gScope) {
        currentScope = this.gScope = gScope;
		gScope.regIdAllocator = new RegIdAllocator();
        this.gBList = gBList;
    }
	
    @Override
    public void visit(RootNode it) {
		Scope stringScope = new Scope(gScope);
		Scope arrayScope = new Scope(gScope);
		gScope.addType("int", new Scope(gScope), it.pos);
		gScope.addType("bool", new Scope(gScope), it.pos);
		gScope.addType("void", new Scope(gScope), it.pos);
		gScope.addType("string", stringScope, it.pos);
		gScope.addType("!array", arrayScope, it.pos);
		gScope.defineFunction("print", (new Type("void")).getFuncType(), it.pos);
		gScope.defineParams("print", new ArrayList<Type>(Arrays.asList(new Type("string"))), it.pos);
		gScope.defineFunction("println", (new Type("void")).getFuncType(), it.pos);
		gScope.defineParams("println", new ArrayList<Type>(Arrays.asList(new Type("string"))), it.pos);
		gScope.defineFunction("printInt", (new Type("void")).getFuncType(), it.pos);
		gScope.defineParams("printInt", new ArrayList<Type>(Arrays.asList(new Type("int"))), it.pos);
		gScope.defineFunction("printlnInt", (new Type("void")).getFuncType(), it.pos);
		gScope.defineParams("printlnInt", new ArrayList<Type>(Arrays.asList(new Type("int"))), it.pos);
		gScope.defineFunction("getString", (new Type("string")).getFuncType(), it.pos);
		gScope.defineParams("getString", new ArrayList<Type>(), it.pos);
		gScope.defineFunction("getInt", (new Type("int")).getFuncType(), it.pos);
		gScope.defineParams("getInt", new ArrayList<Type>(), it.pos);
		gScope.defineFunction("toString", (new Type("string")).getFuncType(), it.pos);
		gScope.defineParams("toString", new ArrayList<Type>(Arrays.asList(new Type("int"))), it.pos);
		
		stringScope.defineFunction("length", (new Type("int")).getFuncType(), it.pos);
		stringScope.defineParams("length", new ArrayList<Type>(), it.pos);
		stringScope.defineFunction("substring", (new Type("string")).getFuncType(), it.pos);
		stringScope.defineParams("substring", new ArrayList<Type>(Arrays.asList(new Type("int"), new Type("int"))), it.pos);
		stringScope.defineFunction("parseInt", (new Type("int")).getFuncType(), it.pos);
		stringScope.defineParams("parseInt", new ArrayList<Type>(), it.pos);
		stringScope.defineFunction("ord", (new Type("int")).getFuncType(), it.pos);
		stringScope.defineParams("ord", new ArrayList<Type>(Arrays.asList(new Type("int"))), it.pos);
		
		arrayScope.defineFunction("size", (new Type("int")).getFuncType(), it.pos);
		arrayScope.defineParams("size", new ArrayList<Type>(), it.pos);

		inInit = true;
		inMainInit = true;
		it.funcDefs.forEach(fd -> fd.accept(this));
        it.classDefs.forEach(d -> d.accept(this));
		inInit = false;
		inMainInit = false;
		
		inVarInit = true;
        it.classDefs.forEach(d -> d.accept(this));
		inVarInit = false;
		
		it.Defs.forEach(d -> d.accept(this));
		if (!gScope.containsFunction("main", false))
			throw new semanticError("main() function not found. ", it.pos);
		if (!gScope.getTypeFunction("main", false).isInt())
			throw new semanticError("main() function must return int. ", it.pos);
		if (gScope.getParams("main", false).size() > 0)
			throw new semanticError("main() function must have no param. ", it.pos);
    }
	
	@Override
    public void visit(funcDefNode it) {
		if (inInit){
			if (inMainInit){
				if (currentClass != null){
					if (it.name.equals(currentClass.name)){
						if (constructDefined) throw new semanticError("Class constructor redefined. ", it.pos);
						if (it.type != null) throw new semanticError("Function name is the same as class. ", it.pos);
						constructDefined = true;
						it.type = new Type("void");
					}else{
						if (it.type == null) throw new semanticError("Constructor name is diff from class. ", it.pos);
					}
				}
				
				currentScope.defineFunction(it.name, it.type.getFuncType(), it.pos);
				ArrayList<Type> params = new ArrayList<>();
				it.funcParams.forEach(p -> params.add(p.type));
				currentScope.defineParams(it.name, params, it.pos);
			}
		}
		else{
			//System.out.println(it.name);
		
			currentScope = new Scope(currentScope);
			currentScope.regIdAllocator = new RegIdAllocator();
			it.scope = currentScope;

			it.funcParams.forEach(p -> p.accept(this));
			returnType = it.type;
			haveReturn = false;
			inFunction = true;
			it.block.accept(this);
			inFunction = false;
			if (!haveReturn){
				if (!it.name.equals("main") && !it.type.isVoid()) throw new semanticError("Function has no return. ", it.pos);
				if (it.name.equals("main")){
					gBList.haveNoReturn = true;
				}
			}

			currentScope = currentScope.parentScope();
		}
    }
	
	@Override
    public void visit(funcParamNode it) {
		it.scope = currentScope;

		it.regId = currentScope.defineVariable(it.name, it.type, it.pos, 1);
    }

    @Override
    public void visit(classDefNode it) {
		
		if (it.name.equals("main")) 
			throw new semanticError("Class named main. ", it.pos);
		currentClass = new Type(it.name);
		
		if (inInit){
			currentScope = new Scope(currentScope);
			it.scope = currentScope;

			constructDefined = false;
			it.funcDefs.forEach(fd -> fd.accept(this));
			gScope.addType(it.name, currentScope, it.pos);
		}
		else if (inVarInit)
		{
			currentScope = gScope.getScopeFromName(it.name, it.pos);
			it.varDefs.forEach(vd -> vd.accept(this));
		}
		else {
			currentScope = gScope.getScopeFromName(it.name, it.pos);
			it.funcDefs.forEach(fd -> fd.accept(this));
		}
		
		currentScope = gScope;
		currentClass = null;
    }

    @Override
    public void visit(varListStmtNode it) {
		it.scope = currentScope;

        if (!it.stmts.isEmpty()) 
            for (StmtNode stmt : it.stmts) stmt.accept(this);
    }

    @Override
    public void visit(varDefStmtNode it) {
		it.scope = currentScope;

		if (gScope.containsType(it.name)) 
			throw new semanticError("Variable and class name is dumplicated: " + it.name, it.pos);
		
		if (!gScope.containsType(it.type.name)) 
			throw new semanticError("Vardef types havent defined: " + it.type.name, it.pos);
			
		if (it.type.name.equals("void"))
			throw new semanticError("Vardef type cannot be: " + it.type.name, it.pos);
		
        if (it.init != null) {
            it.init.accept(this);
            if (!it.init.type.equals(it.type)){
                if ((it.type.dimension > 0 || it.type.isClass()) && it.init.type.isNull());
				else throw new semanticError("Vardef types not match. ", it.init.pos);
			}
        }
		if (currentScope == gScope){
			gBList.globals.add(0);
		}
        
		if (gScope == currentScope) currentScope.defineVariable(it.name, it.type, it.pos, 2);
		else currentScope.defineVariable(it.name, it.type, it.pos, 1);
    }

    @Override
    public void visit(returnStmtNode it) {
		it.scope = currentScope;

		if (!inFunction)
			throw new semanticError("Cant return coz it is not in a function. ", it.pos);

		haveReturn = true;
        if (it.value != null) {
            it.value.accept(this);
			if (!returnType.equals(it.value.type)){
				if ((returnType.dimension > 0 || returnType.isClass()) && it.value.type.isNull());
				else throw new semanticError("Function return type unmatch. ", it.pos);
			}
        }else{
			if (!returnType.isVoid())
				throw new semanticError("Function return type unmatch. ", it.pos);
		}
    }

    @Override
    public void visit(blockStmtNode it) {
		it.scope = currentScope;

        if (!it.stmts.isEmpty()) {
            currentScope = new Scope(currentScope);
            for (StmtNode stmt : it.stmts) stmt.accept(this);
            currentScope = currentScope.parentScope();
        }
    }

    @Override
    public void visit(exprStmtNode it) {
		it.scope = currentScope;

        it.expr.accept(this);
    }
	
	@Override
    public void visit(breakStmtNode it) {
		it.scope = currentScope;

        if (loopDepth <= 0)
			throw new semanticError("Cant break coz it is not in a loop. ", it.pos);
    }
	
	@Override
    public void visit(continueStmtNode it) {
		it.scope = currentScope;

        if (loopDepth <= 0)
			throw new semanticError("Cant continue coz it is not in a loop. ", it.pos);
    }

	@Override
    public void visit(whileStmtNode it) {
		it.scope = currentScope;

        if (it.condition != null){
			it.condition.accept(this);
			if (!it.condition.type.isBool())
				throw new semanticError("While condition should be a bool",
						it.condition.pos);
		}
		currentScope = new Scope(currentScope);
		loopDepth++;
		if (it.Stmt != null) it.Stmt.accept(this);
		loopDepth--;
		currentScope = currentScope.parentScope();
    }

    @Override
    public void visit(ifStmtNode it) {
		it.scope = currentScope;

        it.condition.accept(this);
        if (!it.condition.type.isBool())
            throw new semanticError("If condition should be a bool",
                    it.condition.pos);
		currentScope = new Scope(currentScope);
        if (it.thenStmt != null) it.thenStmt.accept(this);
		currentScope = currentScope.parentScope();
		currentScope = new Scope(currentScope);
        if (it.elseStmt != null) it.elseStmt.accept(this);
		currentScope = currentScope.parentScope();
    }
	
    @Override
    public void visit(forStmtNode it) {
		it.scope = currentScope;

        if (it.condition != null){
			it.condition.accept(this);
			if (!it.condition.type.isBool())
				throw new semanticError("For condition should be a bool",
						it.condition.pos);
		}
        if (it.init != null) it.init.accept(this);
        if (it.incr != null) it.incr.accept(this);
		
		currentScope = new Scope(currentScope);
		loopDepth++;
		if (it.Stmt != null) it.Stmt.accept(this);
		loopDepth--;
		currentScope = currentScope.parentScope();
    }

    @Override
    public void visit(callExprNode it) {
		it.Params.forEach(p -> p.accept(this));
		ExprNode func = it.Params.get(0);
		if (!func.type.isFunc)
			throw new semanticError("callFunc : it is not a function. ", it.pos);
		
		Scope tempScope = currentScope;
		if (func.parent != null)
			currentScope = gScope.getScopeFromName(func.parent.name, it.pos);
		it.scope = currentScope;
		
		ArrayList<Type> tList = currentScope.getParams(func.funcName, true);
		if (it.Params.size() - 1 != tList.size())
			throw new semanticError("callFunc : params numbers unmatch. ", it.pos);
		for (int i = 0; i < tList.size(); i++){
			if (!it.Params.get(i + 1).type.equals(tList.get(i))){
				if ((tList.get(i).dimension > 0 || tList.get(i).isClass()) && it.Params.get(i + 1).type.isNull());
				else throw new semanticError("callFunc : params types unmatch. ", it.pos);
			}
		}
		it.type = new Type(it.Params.get(0).type);
		currentScope = tempScope;
    }

    @Override
    public void visit(assignExprNode it) {
        it.rhs.accept(this);
        it.lhs.accept(this);
		if (!it.lhs.left) throw new semanticError("Assign: should be a leftValue. ", it.pos);
        if (!it.rhs.type.equals(it.lhs.type)){
            if ((it.lhs.type.dimension > 0 || it.lhs.type.isClass()) && it.rhs.type.isNull());
			else throw new semanticError("Assign type not match. ", it.pos);
		}
        it.type = new Type(it.lhs.type);
    }
	
	@Override
    public void visit(suffixExprNode it) {
        it.hs.accept(this);
		if (!it.hs.left) throw new semanticError("suffixExpr: should be a leftValue. ", it.pos);
		if (!it.hs.type.isInt()) throw new semanticError("suffixExpr: should be an int. ", it.pos);
		it.type = new Type("int");
    }
	
    @Override
    public void visit(prefixExprNode it) {
        it.hs.accept(this);
		switch (it.opCode){
			case addadd:
			case subsub:
				it.left = true;
			case add:
			case sub:
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
					if ((it.lhs.type.dimension > 0 || it.lhs.type.isClass()) && it.rhs.type.isNull());
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
		it.left = true;
	}
	
	@Override
    public void visit(memberExprNode it) {
		it.hs.accept(this);
		Scope tempScope = currentScope;
		if (it.hs.type.dimension > 0){
			currentScope = gScope.getScopeFromName("!array", it.pos);
		}else currentScope = gScope.getScopeFromName(it.hs.type.name, it.pos);
		notInClass = false;
		
		it.member.accept(this);
		notInClass = true;
		
		currentScope = tempScope;
		it.type = it.member.type;
		it.funcName = it.member.funcName;
		if (it.hs.type.dimension > 0) it.parent = new Type("!array");
		else it.parent = it.hs.type;
		it.left = it.member.left;
	}

	@Override
    public void visit(newExprNode it) {
		if (it.type.name.equals("void"))
			throw new semanticError("Cannot new void. ", it.pos);
		for (ExprNode e : it.Exprs){
			e.accept(this);
			if (!e.type.isInt())
				throw new semanticError("when new, index should be an int. ", it.pos);
		}
	}

    @Override
    public void visit(literalExprNode it) {
		it.scope = currentScope;
	}

	@Override
    public void visit(thisExprNode it) {
        if (currentClass == null)
			throw new semanticError("this should be in a class. ", it.pos);
		it.type = currentClass;
    }

    @Override
    public void visit(varExprNode it) {
		it.scope = currentScope;
		Scope nowScope = currentScope;
		boolean found = false;
		while (nowScope != null){
			if (nowScope.containsVariable(it.name, false)){
				it.type = nowScope.getTypeVariable(it.name, false);
				it.left = true;
				found = true;
				break;
			}
			else if (nowScope.containsFunction(it.name, false)){
				it.type = nowScope.getTypeFunction(it.name, false);
				it.funcName = it.name;
				found = true;
				break;
			}
			if (notInClass == false) break;
			nowScope = nowScope.parentScope();
		}
		if (!found){
			throw new semanticError("Variable not defined. ", it.pos);
		}
    }
}
