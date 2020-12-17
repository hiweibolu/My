// Generated from D:/Study/Compiler\My.g4 by ANTLR 4.9
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MyParser}.
 */
public interface MyListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MyParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(MyParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(MyParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link MyParser#varDef}.
	 * @param ctx the parse tree
	 */
	void enterVarDef(MyParser.VarDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyParser#varDef}.
	 * @param ctx the parse tree
	 */
	void exitVarDef(MyParser.VarDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link MyParser#suite}.
	 * @param ctx the parse tree
	 */
	void enterSuite(MyParser.SuiteContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyParser#suite}.
	 * @param ctx the parse tree
	 */
	void exitSuite(MyParser.SuiteContext ctx);
	/**
	 * Enter a parse tree produced by {@link MyParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(MyParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(MyParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MyParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(MyParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(MyParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MyParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(MyParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(MyParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link MyParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(MyParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(MyParser.LiteralContext ctx);
}