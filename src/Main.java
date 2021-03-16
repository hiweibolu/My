import AST.RootNode;
import Frontend.ASTBuilder;
import Frontend.SemanticChecker;
import Parser.MxLexer;
import Parser.MxParser;
import Util.MxErrorListener;
import Util.error.error;
import Util.globalScope;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import IR.IRBlockList;
import IR.IRBuilder;

import java.io.FileInputStream;
import java.io.InputStream;


public class Main {
    public static void main(String[] args) throws Exception{
		boolean onlySemantic = false;
		InputStream input = System.in;
		for (String arg : args) {
			switch (arg) {
				case "-semantic":
					onlySemantic = true;
					break;
				case "-test":
					String name = "test1.mx";
					input = new FileInputStream(name);
					break;
			}
		}

        try {
            RootNode ASTRoot;
            globalScope gScope = new globalScope(null);

            MxLexer lexer = new MxLexer(CharStreams.fromStream(input));
            lexer.removeErrorListeners();
            lexer.addErrorListener(new MxErrorListener());
            MxParser parser = new MxParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(new MxErrorListener());
            ParseTree parseTreeRoot = parser.program();
            ASTBuilder astBuilder = new ASTBuilder(gScope);
            ASTRoot = (RootNode)astBuilder.visit(parseTreeRoot);

            IRBlockList gIRList = new IRBlockList();
            new SemanticChecker(gIRList, gScope).visit(ASTRoot);

			if (!onlySemantic){
            	new IRBuilder(gIRList, gScope).visit(ASTRoot);
				//gIRList.initASM();
				gIRList.print();
				//gIRList.printASM();
			}

            /*mainFn f = new mainFn();
            new IRBuilder(f, gScope).visit(ASTRoot);
            // new IRPrinter(System.out).visitFn(f);

            AsmFn asmF = new AsmFn();
            new InstSelector(asmF).visitFn(f);
            new RegAlloc(asmF).work();
            new AsmPrinter(asmF, System.out).print();*/
        } catch (error er) {
            System.err.println(er.toString());
            throw new RuntimeException();
        }
    }
}