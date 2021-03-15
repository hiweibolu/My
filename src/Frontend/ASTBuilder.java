package Frontend;

import AST.*;
import Parser.MxBaseVisitor;
import Parser.MxParser;
import Util.Type;
import Util.globalScope;
import Util.position;
import org.antlr.v4.runtime.ParserRuleContext;
import AST.binaryExprNode.binaryOpType;
import AST.suffixExprNode.suffixOpType;
import AST.prefixExprNode.prefixOpType;
import Util.error.semanticError;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {

    private globalScope gScope;
    public ASTBuilder(globalScope gScope) {
        this.gScope = gScope;
    }

    Type intType, boolType;

    @Override public ASTNode visitProgram(MxParser.ProgramContext ctx) {
		RootNode root = new RootNode(new position(ctx));
		for (MxParser.DefinitionContext def : ctx.definition()) {
			if (def.varDef() != null) {
				ASTNode tmp = (ASTNode)visit(def.varDef());
				root.varDefs.add(tmp);
				root.Defs.add(tmp);
			}
			if (def.funcDef() != null) {
				ASTNode tmp = (ASTNode)visit(def.funcDef());
				root.funcDefs.add(tmp);
				root.Defs.add(tmp);
			}
			if (def.classDef() != null) {
				ASTNode tmp = (ASTNode)visit(def.classDef());
				root.classDefs.add(tmp);
				root.Defs.add(tmp);
			}
		}
		/*ctx.varDef().forEach(d -> root.varDefs.add((ASTNode) visit(d)));
		ctx.funcDef().forEach(d -> root.funcDefs.add((ASTNode) visit(d)));
		ctx.classDef().forEach(d -> root.classDefs.add((ASTNode) visit(d)));*/
        return root;
    }
	
    @Override public ASTNode visitClassDef(MxParser.ClassDefContext ctx) {
        classDefNode classDef = new classDefNode(new position(ctx), ctx.Identifier().toString());
        ctx.varDef().forEach(vd -> classDef.varDefs.add((varListStmtNode) visit(vd)));
        ctx.funcDef().forEach(fd -> classDef.funcDefs.add((funcDefNode) visit(fd)));
        return classDef;
    }
	
	public Type getTypeFromTypeContext(MxParser.TypeContext ctx){
		return new Type(ctx.basicType().getText(), ctx.LeftBracket().size());
	}

    @Override public ASTNode visitVarDef(MxParser.VarDefContext ctx) {
		varListStmtNode varListStmt = new varListStmtNode(new position(ctx));
		
		for (MxParser.VarParamContext stmt : ctx.varParamList().varParam()) {
			ExprNode expr = null;
			if (stmt.expression() != null) expr = (ExprNode)visit(stmt.expression());

			varListStmt.stmts.add(new varDefStmtNode(getTypeFromTypeContext(ctx.type()),
						stmt.Identifier().toString(),
						expr, new position(stmt)));

		}
		return varListStmt;
    }
	
	@Override public ASTNode visitFuncDef(MxParser.FuncDefContext ctx) {
		Type type = null;
        if (ctx.type() != null) type = getTypeFromTypeContext(ctx.type());
		
		funcDefNode funcDef = new funcDefNode(new position(ctx), ctx.Identifier().toString(), 
			type, (blockStmtNode) visit(ctx.suite()));
		
		if (ctx.funcParamList() != null) {
            for (ParserRuleContext param : ctx.funcParamList().funcParam())
                funcDef.funcParams.add((funcParamNode) visit(param));
        }

        return funcDef;
    }
	
	@Override public ASTNode visitFuncParam(MxParser.FuncParamContext ctx) {
        return new funcParamNode(getTypeFromTypeContext(ctx.type()), ctx.Identifier().toString(), new position(ctx));
    }

    @Override public ASTNode visitSuite(MxParser.SuiteContext ctx) {
        blockStmtNode node = new blockStmtNode(new position(ctx));

        if (!ctx.statement().isEmpty()) {
            for (ParserRuleContext stmt : ctx.statement()) {
                StmtNode tmp = (StmtNode)visit(stmt);
                if (tmp != null) node.stmts.add(tmp);
            }
        }
        return node;
    }

    @Override public ASTNode visitBlock(MxParser.BlockContext ctx) {
        return visit(ctx.suite());
    }

    @Override public ASTNode visitVarDefStmt(MxParser.VarDefStmtContext ctx) { return visit(ctx.varDef()); }

    @Override public ASTNode visitIfStmt(MxParser.IfStmtContext ctx) {
        StmtNode thenStmt = (StmtNode)visit(ctx.trueStmt), elseStmt = null;
        ExprNode condition = (ExprNode)visit(ctx.expression());
        if (ctx.falseStmt != null) elseStmt = (StmtNode)visit(ctx.falseStmt);
        return new ifStmtNode(condition, thenStmt, elseStmt, new position(ctx));
    }
	
	@Override public ASTNode visitWhileStmt(MxParser.WhileStmtContext ctx) {
        return new whileStmtNode((ExprNode)visit(ctx.expression()), (StmtNode)visit(ctx.statement()), new position(ctx));
    }
	
	@Override public ASTNode visitForStmt(MxParser.ForStmtContext ctx) {
        StmtNode Stmt = (StmtNode)visit(ctx.statement());
        ExprNode condition = null, init = null, incr = null;
        if (ctx.forInit != null) init = (ExprNode)visit(ctx.forInit);
        if (ctx.forIncr != null) incr = (ExprNode)visit(ctx.forIncr);
        if (ctx.forCondition != null) condition = (ExprNode)visit(ctx.forCondition);
        return new forStmtNode(condition, init, incr, Stmt, new position(ctx));
    }

    @Override public ASTNode visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        ExprNode value = null;
        if (ctx.expression() != null) value = (ExprNode) visit(ctx.expression());
        return new returnStmtNode(value, new position(ctx));
    }
	
	@Override public ASTNode visitBreakStmt(MxParser.BreakStmtContext ctx) {
        return new breakStmtNode(new position(ctx));
    }
	
	@Override public ASTNode visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        return new continueStmtNode(new position(ctx));
    }

    @Override public ASTNode visitPureExprStmt(MxParser.PureExprStmtContext ctx) {
        return new exprStmtNode((ExprNode) visit(ctx.expression()), new position(ctx));
    }

    @Override public ASTNode visitEmptyStmt(MxParser.EmptyStmtContext ctx) {
        return null;
    }

    @Override public ASTNode visitAtomExpr(MxParser.AtomExprContext ctx) {
        return visit(ctx.expression());
    }
	
	@Override public ASTNode visitNewExpr(MxParser.NewExprContext ctx) {
		newExprNode newExpr = new newExprNode(new position(ctx));
		if (ctx.index() != null){
			boolean flag = false;

			for (int i = 0; i < ctx.index().size(); i++){
				if (ctx.index(i).expression() != null){
					if (flag) throw new semanticError("Exprs should be from left to right. ", newExpr.pos);
					newExpr.Exprs.add((ExprNode) visit(ctx.index(i).expression()));
				}else flag = true;
			}
		}
		newExpr.type = new Type(ctx.basicType().getText(), ctx.index().size());
		return newExpr;
    }
	
	@Override public ASTNode visitCallExpr(MxParser.CallExprContext ctx) {
		callExprNode callExpr = new callExprNode(new position(ctx));
		ctx.expression().forEach(e -> callExpr.Params.add((ExprNode) visit(e)));
		return callExpr;
    }
	
	@Override public ASTNode visitIndexExpr(MxParser.IndexExprContext ctx) {
		return new indexExprNode((ExprNode) visit(ctx.expression(0)), (ExprNode) visit(ctx.expression(1)), new position(ctx));
    }
	
    @Override public ASTNode visitMemberExpr(MxParser.MemberExprContext ctx) {
		return new memberExprNode((ExprNode) visit(ctx.expression()), 
			new varExprNode(ctx.Identifier().toString(), new position(ctx.Identifier())),
			new position(ctx));
    }
	
	@Override public ASTNode visitSuffixExpr(MxParser.SuffixExprContext ctx) {
		suffixOpType opCode;
        switch (ctx.op.getText()){
            case "++" : opCode = suffixOpType.addadd; break;
            case "--" : opCode = suffixOpType.subsub; break;
			default : throw new semanticError("Wrong. ", new position(ctx));
        }
        return new suffixExprNode((ExprNode) visit(ctx.expression()), opCode, new position(ctx));
    }
	
	@Override public ASTNode visitPrefixExpr(MxParser.PrefixExprContext ctx) {
		prefixOpType opCode;
        switch (ctx.op.getText()){
            case "+" : opCode = prefixOpType.add; break;
            case "-" : opCode = prefixOpType.sub; break;
            case "++" : opCode = prefixOpType.addadd; break;
            case "--" : opCode = prefixOpType.subsub; break;
            case "!" : opCode = prefixOpType.not; break;
            case "~" : opCode = prefixOpType.no; break;
			default : throw new semanticError("Wrong. ", new position(ctx));
        }
        return new prefixExprNode((ExprNode) visit(ctx.expression()), opCode, new position(ctx));
    }

    @Override public ASTNode visitBinaryExpr(MxParser.BinaryExprContext ctx) {
		binaryOpType opCode;
        switch (ctx.op.getText()){
            case "+" : opCode = binaryOpType.add; break;
            case "-" : opCode = binaryOpType.sub; break;
            case "||" : opCode = binaryOpType.oror; break;
            case "&&" : opCode = binaryOpType.andand; break;
            case "|" : opCode = binaryOpType.or; break;
            case "&" : opCode = binaryOpType.and; break;
            case "^" : opCode = binaryOpType.xor; break;
            case "==" : opCode = binaryOpType.eq; break;
            case "!=" : opCode = binaryOpType.neq; break;
            case ">" : opCode = binaryOpType.ge; break;
            case "<" : opCode = binaryOpType.le; break;
            case ">=" : opCode = binaryOpType.geq; break;
            case "<=" : opCode = binaryOpType.leq; break;
            case "<<" : opCode = binaryOpType.shl; break;
            case ">>" : opCode = binaryOpType.shr; break;
            case "*" : opCode = binaryOpType.mul; break;
            case "/" : opCode = binaryOpType.div; break;
            case "%" : opCode = binaryOpType.mod; break;
			default : throw new semanticError("Wrong. ", new position(ctx));
        }
        return new binaryExprNode((ExprNode) visit(ctx.expression(0)), (ExprNode) visit(ctx.expression(1)),
			opCode, new position(ctx));
    }

    @Override public ASTNode visitAssignExpr(MxParser.AssignExprContext ctx) {
        ExprNode lhs = (ExprNode) visit(ctx.expression(0)),
                 rhs = (ExprNode) visit(ctx.expression(1));
        return new assignExprNode(lhs, rhs, new position(ctx));
    }
	
	@Override public ASTNode visitThisExpr(MxParser.ThisExprContext ctx) {
		return new thisExprNode(new position(ctx));
    }

    @Override public ASTNode visitIdExpr(MxParser.IdExprContext ctx) {
		return new varExprNode(ctx.Identifier().toString(), new position(ctx.Identifier()));
    }

    @Override public ASTNode visitLiteralExpr(MxParser.LiteralExprContext ctx) {
        /*return new constExprNode(Integer.parseInt(ctx.DecimalInteger().toString()),
                                 intType, new position(ctx));*/
		if (ctx.literal().True() != null) return new literalExprNode(new Type("bool"), new position(ctx), ctx);
		if (ctx.literal().False() != null) return new literalExprNode(new Type("bool"), new position(ctx), ctx);
		if (ctx.literal().DecimalInteger() != null) return new literalExprNode(new Type("int"), new position(ctx), ctx);
		if (ctx.literal().StringConstant() != null) return new literalExprNode(new Type("string"), new position(ctx), ctx);
		return new literalExprNode(new Type("null"), new position(ctx), ctx);
    }

}
