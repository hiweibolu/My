package IR;

import AST.*;
import Util.Scope;
import Util.Type;
import Util.error.semanticError;
import Util.globalScope;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import IR.*;
import IR.IRLine.lineType;

public class IRBuilder implements ASTVisitor {
    private Scope currentScope;
    private globalScope gScope;
    private Type returnType = null, currentClass = null;
	private int loopDepth = 0;
	private boolean haveReturn = false, inFunction = false, notInClass = true;
	private HashMap<String, Scope> classesScope;
	private boolean inInit = false, inMainInit = false, inVarInit = false, constructDefined = false;

    private IRBlockList gBList;
	private IRBlock currentBlock;
	private int labelNumber = 0;
	private int loopStart = 0, loopEnd = 0, loopCont = 0;
	private int ifElse = 0, ifEnd = 0;

	private ArrayList<ASTNode> gVarDefs;

	public int labelAlloc(){
		//return ++labelNumber;
		return currentBlock.labelAlloc();
	}

    public IRBuilder(IRBlockList gBList, globalScope gScope) {
        currentScope = this.gScope = gScope;
        this.gBList = gBList;
    }
	
    @Override
    public void visit(RootNode it) {
		/*inInit = true;
		inMainInit = true;
		it.funcDefs.forEach(fd -> fd.accept(this));
        it.classDefs.forEach(d -> d.accept(this));
		inInit = false;
		inMainInit = false;
		
		inVarInit = true;
        it.classDefs.forEach(d -> d.accept(this));
		inVarInit = false;*/
		gVarDefs = it.varDefs;
		
		it.Defs.forEach(d -> d.accept(this));
    }
	
	@Override
    public void visit(funcDefNode it) {
		currentBlock = new IRBlock();
		currentBlock.name = it.func_name;
		currentBlock.regIdAllocator = it.scope.regIdAllocator;
		currentBlock.returnLabel = labelAlloc();
		gBList.blocks.add(currentBlock);
		currentBlock.id = gBList.blocks.size() - 1;
		IRLine line = new IRLine(lineType.FUNC);
		line.func = it.func_name;
		currentBlock.lines.add(line);

		if (currentClass == null){
			for (int i = 0; i < it.funcParams.size(); i++){
				line =  new IRLine(lineType.MOVE);
				line.args.add(it.funcParams.get(i).regId);
				if (i < 8) line.args.add(new IRRegIdentifier(i + 10, 0, false));
				else line.args.add(new IRRegIdentifier(i - 8, 4, false));
				currentBlock.lines.add(line);
			}
			currentBlock.args_num = it.funcParams.size();

			inMainInit = true;
			if (it.name.equals("main")) gVarDefs.forEach(d -> d.accept(this));
			inMainInit = false;

			it.block.accept(this);

			if (it.name.equals("main") && gBList.haveNoReturn){
				line = new IRLine(lineType.MOVE);
				line.args.add(new IRRegIdentifier(10, 0, false));
				line.args.add(new IRRegIdentifier(0, 0, false));
				currentBlock.lines.add(line);
				line = new IRLine(lineType.JUMP);
				line.label = currentBlock.returnLabel;
				currentBlock.lines.add(line);
			}
		} else {
			IRRegIdentifier this_reg = new IRRegIdentifier(0, 1, false);
			line = new IRLine(lineType.MOVE);
			line.args.add(this_reg);
			line.args.add(new IRRegIdentifier(10, 0, false));
			currentBlock.lines.add(line);

			for (int i = 0; i < it.funcParams.size(); i++){
				line = new IRLine(lineType.MOVE);
				line.args.add(it.funcParams.get(i).regId);
				if (i + 1 < 8) line.args.add(new IRRegIdentifier(i + 1 + 10, 0, false));
				else line.args.add(new IRRegIdentifier(i + 1 - 8, 4, false));
				currentBlock.lines.add(line);
			}
			currentBlock.args_num = it.funcParams.size() + 1;
			it.block.accept(this);
		}
		line = new IRLine(lineType.LABEL);
		line.label = currentBlock.returnLabel;
		currentBlock.lines.add(line);
    }
	
	@Override
    public void visit(funcParamNode it) {
    }

    @Override
    public void visit(classDefNode it) {

/*System.out.println("hello");
System.out.println(it.name);
System.out.println(it.scope.variables.size());
System.out.println("hello");*/

		currentClass = new Type(it.name);
		it.funcDefs.forEach(fd -> fd.accept(this));		
		currentClass = null;
    }

    @Override
    public void visit(varListStmtNode it) {
        if (!it.stmts.isEmpty()) 
            for (StmtNode stmt : it.stmts) stmt.accept(this);
    }

    @Override
    public void visit(varDefStmtNode it) {	
		if (it.scope == gScope && !inMainInit){
		}else{
			if (it.init != null) {
				it.init.accept(this);
				
				IRLine line = new IRLine(lineType.MOVE);
				line.args.add(it.scope.getRegIdVariable(it.name, false));
				line.args.add(it.init.regId);
				currentBlock.lines.add(line);
			}
		}
    }

    @Override
    public void visit(returnStmtNode it) {
        if (it.value != null) {
            it.value.accept(this);
			
			IRLine line = new IRLine(lineType.MOVE);
			line.args.add(new IRRegIdentifier(10, 0, false));
			line.args.add(it.value.regId);
			currentBlock.lines.add(line);
        }
		IRLine line = new IRLine(lineType.JUMP);
		line.label = currentBlock.returnLabel;
		currentBlock.lines.add(line);
    }

    @Override
    public void visit(blockStmtNode it) {
        if (!it.stmts.isEmpty()) 
            for (StmtNode stmt : it.stmts) stmt.accept(this);
    }

    @Override
    public void visit(exprStmtNode it) {
        it.expr.accept(this);
    }
	
	@Override
    public void visit(breakStmtNode it) {
		IRLine line = new IRLine(lineType.JUMP);
		line.label = loopEnd;
		currentBlock.lines.add(line);
    }
	
	@Override
    public void visit(continueStmtNode it) {
		IRLine line = new IRLine(lineType.JUMP);
		line.label = loopCont;
		currentBlock.lines.add(line);
    }

	@Override
    public void visit(whileStmtNode it) {
		int oldLoopStart = loopStart, oldLoopEnd = loopEnd;
		int oldLoopCont = loopCont;
		loopCont = loopStart = labelAlloc();
		loopEnd = labelAlloc();

		IRLine line = new IRLine(lineType.LABEL);
		line.label = loopStart;
		currentBlock.lines.add(line);

        if (it.condition != null){
			it.condition.accept(this);

			line = new IRLine(lineType.BNEQ);
			line.args.add(it.condition.regId);
			line.args.add(new IRRegIdentifier(0, 0, false));
			line.label = loopEnd;
			currentBlock.lines.add(line);
		}

		if (it.Stmt != null) it.Stmt.accept(this);

		line = new IRLine(lineType.JUMP);
		line.label = loopStart;
		currentBlock.lines.add(line);
		line = new IRLine(lineType.LABEL);
		line.label = loopEnd;
		currentBlock.lines.add(line);

		loopCont = oldLoopCont;
		loopStart = oldLoopStart;
		loopEnd = oldLoopEnd;
    }

    @Override
    public void visit(ifStmtNode it) {
		int oldIfElse = ifElse, oldIfEnd = ifEnd;
		ifEnd = labelAlloc();
		if (it.elseStmt != null) ifElse = labelAlloc();

        it.condition.accept(this);
		IRLine line;
		if (it.condition.regId.cst){
			if (it.condition.regId.val == 0){
				line = new IRLine(lineType.JUMP);
				if (it.elseStmt != null) line.label = ifElse;
				else line.label = ifEnd;
				currentBlock.lines.add(line);
			}else{
			}
		}else{
			line = new IRLine(lineType.BNEQ);
			line.args.add(it.condition.regId);
			line.args.add(new IRRegIdentifier(0, 0, false));
			if (it.elseStmt != null) line.label = ifElse;
			else line.label = ifEnd;
			currentBlock.lines.add(line);
		}

        if (it.thenStmt != null){
			it.thenStmt.accept(this);

			if (it.elseStmt != null){
				line = new IRLine(lineType.JUMP);
				line.label = ifEnd;
				currentBlock.lines.add(line);
			}
		}
        if (it.elseStmt != null){
			line = new IRLine(lineType.LABEL);
			line.label = ifElse;
			currentBlock.lines.add(line);
			
			it.elseStmt.accept(this);
		}

		line = new IRLine(lineType.LABEL);
		line.label = ifEnd;
		currentBlock.lines.add(line);

		ifElse = oldIfElse;
		ifEnd = oldIfEnd;
    }
	
    @Override
    public void visit(forStmtNode it) {
		int oldLoopStart = loopStart, oldLoopEnd = loopEnd;
		int oldLoopCont = loopCont;
		loopStart = labelAlloc();
		loopEnd = labelAlloc();
		loopCont = labelAlloc();

        if (it.init != null) it.init.accept(this);
		IRLine line = new IRLine(lineType.LABEL);
		line.label = loopStart;
		currentBlock.lines.add(line);

        if (it.condition != null){
			it.condition.accept(this);

			line = new IRLine(lineType.BNEQ);
			line.args.add(it.condition.regId);
			line.args.add(new IRRegIdentifier(0, 0, false));
			line.label = loopEnd;
			currentBlock.lines.add(line);
		}
		if (it.Stmt != null) it.Stmt.accept(this);
		line = new IRLine(lineType.LABEL);
		line.label = loopCont;
		currentBlock.lines.add(line);
        if (it.incr != null) it.incr.accept(this);
		
		line = new IRLine(lineType.JUMP);
		line.label = loopStart;
		currentBlock.lines.add(line);
		line = new IRLine(lineType.LABEL);
		line.label = loopEnd;
		currentBlock.lines.add(line);

		loopStart = oldLoopStart;
		loopEnd = oldLoopEnd;
		loopCont = oldLoopCont;
    }

    @Override
    public void visit(callExprNode it) {
		if (currentBlock.maxParamsNumber + 1 < it.Params.size())
			currentBlock.maxParamsNumber = it.Params.size() - 1;

		it.Params.forEach(p -> p.accept(this));
		ExprNode func = it.Params.get(0);

		int have_ptr = it.scope.getInClassFunction(func.funcName, true);
		if (it.Params.size() > 0){
			for (int i = it.Params.size() - 1; i > 0; i--){
				//it.Params.get(i).accept(this);
				IRLine line = new IRLine(lineType.MOVE);
				line.args.add(new IRRegIdentifier(i - 1 + have_ptr, 3, false));
				line.args.add(it.Params.get(i).regId);
				currentBlock.lines.add(line);
			}
		}

		if (have_ptr == 1){
			if (func.parent != null){
				//it.Params.get(0).accept(this);
				IRLine line = new IRLine(lineType.MOVE);
				line.args.add(new IRRegIdentifier(0, 3, false));
				line.args.add(it.Params.get(0).regId);
				currentBlock.lines.add(line);
			}else{
				IRLine line = new IRLine(lineType.MOVE);
				line.args.add(new IRRegIdentifier(0, 3, false));
				line.args.add(new IRRegIdentifier(0, 1, false));
				currentBlock.lines.add(line);
			}
		}
		IRLine line = new IRLine(lineType.CALL);
		if (have_ptr == 0) line.func = it.Params.get(0).funcName;
		else line.func = it.scope.getNameFunction(it.Params.get(0).funcName, true);
		currentBlock.lines.add(line);

		it.regId = currentBlock.regIdAllocator.alloc(5);
		line = new IRLine(lineType.MOVE);
		line.args.add(it.regId);
		line.args.add(new IRRegIdentifier(10, 0, false));
		currentBlock.lines.add(line);

		/*ExprNode func = it.Params.get(0);
		
		Scope tempScope = currentScope;
		if (func.parent != null)
			currentScope = gScope.getScopeFromName(func.parent.name, it.pos);
		ArrayList<Type> tList = currentScope.getParams(func.funcName, true);
		it.type = new Type(it.Params.get(0).type);
		currentScope = tempScope;*/

    }

    @Override
    public void visit(assignExprNode it) {
        it.rhs.accept(this);
        it.lhs.accept(this);

		IRLine line = new IRLine(lineType.MOVE);
		line.args.add(it.lhs.regId);
		line.args.add(it.rhs.regId);
		currentBlock.lines.add(line);

		it.regId = it.lhs.regId;
    }
	
	@Override
    public void visit(suffixExprNode it) {
        it.hs.accept(this);

		it.regId = currentBlock.regIdAllocator.alloc(5);
		IRLine line = new IRLine(lineType.MOVE);
		line.args.add(it.regId);
		line.args.add(it.hs.regId);
		currentBlock.lines.add(line);

		line = new IRLine(lineType.ADDI);
		line.args.add(it.hs.regId);
		line.args.add(it.hs.regId);
		switch (it.opCode){
			case addadd:
				line.args.add(new IRRegIdentifier(1, 8, false));
				break;
			case subsub:
				line.args.add(new IRRegIdentifier(-1, 8, false));

		}
		currentBlock.lines.add(line);
    }
	
    @Override
    public void visit(prefixExprNode it) {
        it.hs.accept(this);
		IRLine line;
		switch (it.opCode){
			case addadd:
				it.regId = it.hs.regId;
				line = new IRLine(lineType.ADDI);
				line.args.add(it.regId);
				line.args.add(it.regId);
				line.args.add(new IRRegIdentifier(1, 8, false));
				currentBlock.lines.add(line);
				break;
			case subsub:
				it.regId = it.hs.regId;
				line = new IRLine(lineType.ADDI);
				line.args.add(it.regId);
				line.args.add(it.regId);
				line.args.add(new IRRegIdentifier(-1, 8, false));
				currentBlock.lines.add(line);
				break;
			case add:
				it.regId = it.hs.regId;
				break;
			case sub:
				it.regId = currentBlock.regIdAllocator.alloc(5);
				line = new IRLine(lineType.NEG);
				line.args.add(it.regId);
				line.args.add(it.hs.regId);
				currentBlock.lines.add(line);
				break;
			case no:
				it.regId = currentBlock.regIdAllocator.alloc(5);
				line = new IRLine(lineType.NOT);
				line.args.add(it.regId);
				line.args.add(it.hs.regId);
				currentBlock.lines.add(line);
				break;
			case not:
				it.regId = currentBlock.regIdAllocator.alloc(5);
				line = new IRLine(lineType.LOGICNOT);
				line.args.add(it.regId);
				line.args.add(it.hs.regId);
				currentBlock.lines.add(line);
		}
    }

    @Override
    public void visit(binaryExprNode it) {
		it.regId = currentBlock.regIdAllocator.alloc(5);
		IRLine line = null;
		int short_circuit, expr_end;
		switch (it.opCode){// short-circuit
			case andand:
				if (it.and_short == null) short_circuit = labelAlloc();
				else{
					short_circuit = it.and_short;
					it.shorted = true;
				}
				it.lhs.and_short = short_circuit;

				it.lhs.accept(this);
				int pos = currentBlock.lines.size();
				it.rhs.accept(this);
				if (it.lhs.regId.cst && it.rhs.regId.cst){
					IRRegIdentifier regId = new IRRegIdentifier(
						it.lhs.regId.val & it.rhs.regId.val, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}

				if (!it.lhs.shorted){
					line = new IRLine(lineType.BNEQ);
					line.args.add(it.lhs.regId);
					line.args.add(new IRRegIdentifier(0, 0, false));
					line.label = short_circuit;
					currentBlock.lines.add(pos, line);
				}

				//it.rhs.accept(this);
				line = new IRLine(lineType.BNEQ);
				line.args.add(it.rhs.regId);
				line.args.add(new IRRegIdentifier(0, 0, false));
				line.label = short_circuit;
				currentBlock.lines.add(line);

				if (it.and_short == null){
					expr_end = labelAlloc();
					line = new IRLine(lineType.LOAD);
					line.args.add(it.regId);
					line.args.add(new IRRegIdentifier(1, 8, false));
					currentBlock.lines.add(line);
					line = new IRLine(lineType.JUMP);
					line.label = expr_end;
					currentBlock.lines.add(line);
					line = new IRLine(lineType.LABEL);
					line.label = short_circuit;
					currentBlock.lines.add(line);
					line = new IRLine(lineType.LOAD);
					line.args.add(it.regId);
					line.args.add(new IRRegIdentifier(0, 8, false));
					currentBlock.lines.add(line);
					line = new IRLine(lineType.LABEL);
					line.label = expr_end;
					currentBlock.lines.add(line);
				}
				return;
			case oror:
				if (it.or_short == null) short_circuit = labelAlloc();
				else{
					short_circuit = it.or_short;
					it.shorted = true;
				}
				it.lhs.or_short = short_circuit;

				it.lhs.accept(this);
				pos = currentBlock.lines.size();
				it.rhs.accept(this);
				if (it.lhs.regId.cst && it.rhs.regId.cst){
					IRRegIdentifier regId = new IRRegIdentifier(
						it.lhs.regId.val | it.rhs.regId.val, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}

				if (!it.lhs.shorted){
					line = new IRLine(lineType.BEQ);
					line.args.add(it.lhs.regId);
					line.args.add(new IRRegIdentifier(0, 0, false));
					line.label = short_circuit;
					currentBlock.lines.add(pos, line);
				}

				line = new IRLine(lineType.BEQ);
				line.args.add(it.rhs.regId);
				line.args.add(new IRRegIdentifier(0, 0, false));
				line.label = short_circuit;
				currentBlock.lines.add(line);
				
				if (it.or_short == null){
					expr_end = labelAlloc();
					line = new IRLine(lineType.LOAD);
					line.args.add(it.regId);
					line.args.add(new IRRegIdentifier(0, 8, false));
					currentBlock.lines.add(line);
					line = new IRLine(lineType.JUMP);
					line.label = expr_end;
					currentBlock.lines.add(line);
					line = new IRLine(lineType.LABEL);
					line.label = short_circuit;
					currentBlock.lines.add(line);
					line = new IRLine(lineType.LOAD);
					line.args.add(it.regId);
					line.args.add(new IRRegIdentifier(1, 8, false));
					currentBlock.lines.add(line);
					line = new IRLine(lineType.LABEL);
					line.label = expr_end;
					currentBlock.lines.add(line);
				}

				return;
		}
        it.lhs.accept(this);
        it.rhs.accept(this);
		if (it.lhs.type.isString() && it.rhs.type.isString()){
			line = new IRLine(lineType.MOVE);
			line.args.add(new IRRegIdentifier(1, 3, false));
			line.args.add(it.rhs.regId);
			currentBlock.lines.add(line);
			line = new IRLine(lineType.MOVE);
			line.args.add(new IRRegIdentifier(0, 3, false));
			line.args.add(it.lhs.regId);
			currentBlock.lines.add(line);
			switch (it.opCode){
				case eq:
					line = new IRLine(lineType.CALL);
					line.func = "my_string_eq";
					currentBlock.lines.add(line);
					break;
				case neq:
					line = new IRLine(lineType.CALL);
					line.func = "my_string_neq";
					currentBlock.lines.add(line);
					break;
				case ge:
					line = new IRLine(lineType.CALL);
					line.func = "my_string_ge";
					currentBlock.lines.add(line);
					break;
				case geq:
					line = new IRLine(lineType.CALL);
					line.func = "my_string_geq";
					currentBlock.lines.add(line);
					break;
				case le:
					line = new IRLine(lineType.CALL);
					line.func = "my_string_le";
					currentBlock.lines.add(line);
					break;
				case leq:
					line = new IRLine(lineType.CALL);
					line.func = "my_string_leq";
					currentBlock.lines.add(line);
					break;
				case add:
					line = new IRLine(lineType.CALL);
					line.func = "my_string_plus";
					currentBlock.lines.add(line);
					break;
			}
			line = new IRLine(lineType.MOVE);
			line.args.add(it.regId);
			line.args.add(new IRRegIdentifier(10, 0, false));
			currentBlock.lines.add(line);
			return;
		}
		boolean flag = it.lhs.regId.cst && it.rhs.regId.cst;
		switch (it.opCode){
			case eq:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						(it.lhs.regId.val == it.rhs.regId.val) ? 1 : 0, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.EQ);
				break;
			case neq:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						(it.lhs.regId.val != it.rhs.regId.val) ? 1 : 0, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.NEQ);
				break;
			case ge:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						(it.lhs.regId.val > it.rhs.regId.val) ? 1 : 0, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.GE);
				break;
			case geq:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						(it.lhs.regId.val >= it.rhs.regId.val) ? 1 : 0, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.GEQ);
				break;
			case le:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						(it.lhs.regId.val < it.rhs.regId.val) ? 1 : 0, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.LE);
				break;
			case leq:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						(it.lhs.regId.val <= it.rhs.regId.val) ? 1 : 0, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.LEQ);
				break;
			case add:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						it.lhs.regId.val + it.rhs.regId.val, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.ADD);
				break;
			case or:
			//case oror:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						it.lhs.regId.val | it.rhs.regId.val, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.OR);
				break;
			case and:
			//case andand:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						it.lhs.regId.val & it.rhs.regId.val, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.AND);
				break;
			case sub:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						it.lhs.regId.val - it.rhs.regId.val, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.SUB);
				break;
			case xor:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						it.lhs.regId.val ^ it.rhs.regId.val, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.XOR);
				break;
			case shl:
				if (flag){
					//System.out.println(it.lhs.regId.val + " " + it.rhs.regId.val);
					IRRegIdentifier regId = new IRRegIdentifier(
						it.lhs.regId.val << it.rhs.regId.val, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.SHL);
				break;
			case shr:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						it.lhs.regId.val >> it.rhs.regId.val, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.SHR);
				break;
			case mul:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						it.lhs.regId.val * it.rhs.regId.val, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.MUL);
				break;
			case div:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						(it.rhs.regId.val != 0) ? it.lhs.regId.val / it.rhs.regId.val : 114514, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.DIV);
				break;
			case mod:
				if (flag){
					IRRegIdentifier regId = new IRRegIdentifier(
						(it.rhs.regId.val != 0) ? it.lhs.regId.val % it.rhs.regId.val : 1919810, 8, false);
					regId.cst = true;
					regId.val = regId.id;
					line = new IRLine(lineType.LOAD);	
					line.args.add(it.regId);
					line.args.add(regId);
					currentBlock.lines.add(line);
					it.regId.cst_agn(regId);
					return;
				}
				line = new IRLine(lineType.MOD);
		}
		line.args.add(it.regId);
		line.args.add(it.lhs.regId);
		line.args.add(it.rhs.regId);
		currentBlock.lines.add(line);
    }
	
	@Override
    public void visit(indexExprNode it) {
		it.lhs.accept(this);
		it.rhs.accept(this);
		IRRegIdentifier temp = currentBlock.regIdAllocator.alloc(5);
		IRLine line = new IRLine(lineType.ADDI);
		line.args.add(temp);
		line.args.add(it.rhs.regId);
		line.args.add(new IRRegIdentifier(1, 8, false));
		currentBlock.lines.add(line);
		IRRegIdentifier regId = currentBlock.regIdAllocator.alloc(5);
		line = new IRLine(lineType.INDEX);
		line.args.add(regId);
		line.args.add(it.lhs.regId);
		line.args.add(temp);
		currentBlock.lines.add(line);

		it.regId = new IRRegIdentifier(regId.id, regId.typ, true);
	}
	
	@Override
    public void visit(memberExprNode it) {
		it.hs.accept(this);
		it.member.pRegId = it.hs.regId;
		it.member.accept(this);
		if (it.member.inClass){
			IRRegIdentifier regId = currentBlock.regIdAllocator.alloc(5);
			IRLine line = new IRLine(lineType.INDEX);
			line.args.add(regId);
			line.args.add(it.hs.regId);
			line.args.add(it.member.regId);
			currentBlock.lines.add(line);

			it.regId = new IRRegIdentifier(regId.id, regId.typ, true);
		}else{
			it.regId = it.hs.regId;
		}
	}

	public IRRegIdentifier newMalloc(newExprNode it, int i){
		if (i >= it.Exprs.size()){
				//if (gBList.class_sizes.containsKey(it.type.name)){
					/*System.out.println(it.type.name);
					System.out.println(gBList.class_sizes.containsKey(it.type.name));
					System.out.println(it.Exprs.size());
					System.out.println(it.type.dimension);
					System.out.println(it.type.isClass() || gBList.class_sizes.containsKey(it.type.name) && it.Exprs.size() == it.type.dimension);

					System.out.println("--------");*/
				//}
			if (it.type.isClass() || gBList.class_sizes.containsKey(it.type.name) && it.Exprs.size() == it.type.dimension){
				IRLine line = new IRLine(lineType.LOAD);
				line.args.add(new IRRegIdentifier(0, 3, false));
				line.args.add(new IRRegIdentifier(gBList.class_sizes.get(it.type.name) << 2, 8, false));
				currentBlock.lines.add(line);
				
				line = new IRLine(lineType.CALL);
				line.func = "malloc";
				currentBlock.lines.add(line);

				IRRegIdentifier nowRegId = currentBlock.regIdAllocator.alloc(5);
				line = new IRLine(lineType.MOVE);
				line.args.add(nowRegId);
				line.args.add(new IRRegIdentifier(10, 0, false));
				currentBlock.lines.add(line);

				Scope temp_scope = gScope.getScopeFromName(it.type.name);
				if (temp_scope.containsFunction(it.type.name, false)){

					line = new IRLine(lineType.CALL);
					line.func = "my_c_" + it.type.name + "_" + it.type.name;
					currentBlock.lines.add(line);
				};
				return nowRegId;
			}else{
				return new IRRegIdentifier(0, 0, false);
			}
		}
		IRRegIdentifier iter = currentBlock.regIdAllocator.alloc(1);
		IRRegIdentifier nowRegId = currentBlock.regIdAllocator.alloc(1);
		IRRegIdentifier ttemp = currentBlock.regIdAllocator.alloc(5);
		IRLine line = new IRLine(lineType.MOVE);
		line.args.add(iter);
		line.args.add(it.Exprs.get(i).regId);
		currentBlock.lines.add(line);

		line = new IRLine(lineType.MOVE);
		line.args.add(ttemp);
		line.args.add(it.Exprs.get(i).regId);
		currentBlock.lines.add(line);

		
		line = new IRLine(lineType.MOVE);
		line.args.add(new IRRegIdentifier(10, 0, false));
		line.args.add(ttemp);
		currentBlock.lines.add(line);

		/*line = new IRLine(lineType.LOAD);
		line.args.add(new IRRegIdentifier(10, 0, false));
		line.args.add(it.Exprs.get(i).regId);
		currentBlock.lines.add(line);*/

		line = new IRLine(lineType.CALL);
		line.func = "my_array_alloc";
		currentBlock.lines.add(line);
		line = new IRLine(lineType.MOVE);
		line.args.add(nowRegId);
		line.args.add(new IRRegIdentifier(10, 0, false));
		currentBlock.lines.add(line);

		if (i < it.Exprs.size() - 1 || 
			it.type.isClass() || gBList.class_sizes.containsKey(it.type.name) && it.Exprs.size() == it.type.dimension){
			int loopStart = labelAlloc(), loopEnd = labelAlloc();
			line = new IRLine(lineType.LABEL);
			line.label = loopStart;
			currentBlock.lines.add(line);
			line = new IRLine(lineType.BNEQ);
			line.args.add(iter);
			line.args.add(new IRRegIdentifier(0, 0, false));
			line.label = loopEnd;
			currentBlock.lines.add(line);

			IRRegIdentifier next_result = newMalloc(it, i + 1);
			
			if (!next_result.equals(new IRRegIdentifier(0, 0, false))){
				IRRegIdentifier result = currentBlock.regIdAllocator.alloc(5);
				line = new IRLine(lineType.INDEX);
				line.args.add(result);
				line.args.add(nowRegId);
				line.args.add(iter);
				currentBlock.lines.add(line);
				line = new IRLine(lineType.MOVE);
				line.args.add(new IRRegIdentifier(result.id, result.typ, true));
				line.args.add(next_result);
				currentBlock.lines.add(line);
			}
			line = new IRLine(lineType.ADDI);
			line.args.add(iter);
			line.args.add(iter);
			line.args.add(new IRRegIdentifier(-1, 8, false));
			currentBlock.lines.add(line);
			line = new IRLine(lineType.JUMP);
			line.label = loopStart;
			currentBlock.lines.add(line);

			line = new IRLine(lineType.LABEL);
			line.label = loopEnd;
			currentBlock.lines.add(line);
		}

		return nowRegId;
	}

	@Override
    public void visit(newExprNode it) {
		for (ExprNode e : it.Exprs){
			e.accept(this);
		}
		it.regId = newMalloc(it, 0);
	}

    @Override
    public void visit(literalExprNode it) {
		it.regId = currentBlock.regIdAllocator.alloc(5);
		IRRegIdentifier regId;
		if (it.ctx.literal().True() != null){
			IRLine line = new IRLine(lineType.LOAD);
			regId = new IRRegIdentifier(1, 8, false);
			regId.cst = true;
			regId.val = 1;
			line.args.add(it.regId);
			line.args.add(regId);
			currentBlock.lines.add(line);
		}else if (it.ctx.literal().DecimalInteger() != null){
			regId = new IRRegIdentifier(
				Integer.parseInt(it.ctx.literal().DecimalInteger().toString()), 8, false);
			regId.cst = true;
			regId.val = regId.id;
			IRLine line = new IRLine(lineType.LOAD);
			line.args.add(it.regId);
			line.args.add(regId);
			currentBlock.lines.add(line);
		}else if (it.ctx.literal().StringConstant() != null){
			IRLine line = new IRLine(lineType.LOADSTRING);
			String s = it.ctx.literal().StringConstant().toString();
			regId = new IRRegIdentifier(
				gBList.addString(s.substring(1, s.length() - 1)), 9, false);
			line.args.add(it.regId);
			line.args.add(regId);
			currentBlock.lines.add(line);
		}else{
			regId = new IRRegIdentifier(0, 8, false);
			regId.cst = true;
			regId.val = 0;
			IRLine line = new IRLine(lineType.LOAD);
			line.args.add(it.regId);
			line.args.add(regId);
			currentBlock.lines.add(line);
		}
		it.regId.cst_agn(regId);
	}

	@Override
    public void visit(thisExprNode it) {
		it.regId = new IRRegIdentifier(0, 1, false);
    }

    @Override
    public void visit(varExprNode it) {
		Scope nowScope = it.scope;
		while (nowScope != null){
			if (nowScope.containsVariable(it.name, false)){
				if (it.pRegId != null){
					it.regId = currentBlock.regIdAllocator.alloc(5);
					IRLine line = new IRLine(lineType.LOAD);
					line.args.add(it.regId);
					line.args.add(new IRRegIdentifier(nowScope.getIdVariable(it.name, false), 8, false));
					currentBlock.lines.add(line);
					it.inClass = true;
				} else{
					if (it.regId.typ == 11){
						IRRegIdentifier temp = currentBlock.regIdAllocator.alloc(5);
						IRLine line = new IRLine(lineType.LOAD);
						line.args.add(temp);
						line.args.add(new IRRegIdentifier(it.regId.id, 8, false));
						currentBlock.lines.add(line);

						IRRegIdentifier regId = currentBlock.regIdAllocator.alloc(5);
						line = new IRLine(lineType.INDEX);
						line.args.add(regId);
						line.args.add(new IRRegIdentifier(0, 1, false));
						line.args.add(temp);
						currentBlock.lines.add(line);

						it.regId = new IRRegIdentifier(regId.id, regId.typ, true);
					}else{
						/*IRRegIdentifier regId = currentBlock.regIdAllocator.alloc(5);
						IRLine line = new IRLine(lineType.MOVE);
						line.args.add(regId);
						line.args.add(it.regId);
						currentBlock.lines.add(line);
						it.regId = regId;*/

					}
				}
				break;
			}
			else if (nowScope.containsFunction(it.name, false)){
				if (it.pRegId != null){
					it.regId = it.pRegId;
				}
				/*it.type = nowScope.getTypeFunction(it.name, false);
				it.funcName = it.name;
				found = true;*/
				break;
			}
			nowScope = nowScope.parentScope();
		}
    }
}
