package Parser;
// Generated from D:/Shared/Compiler-Design-Implementation/My/src/Parser\Mx.g4 by ANTLR 4.9
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		BasicType=1, Int=2, Bool=3, String=4, Null=5, Void=6, True=7, False=8, 
		If=9, Else=10, For=11, While=12, Break=13, Continue=14, Return=15, New=16, 
		Class=17, This=18, Plus=19, Minus=20, Star=21, Divide=22, Modulo=23, Greater=24, 
		Less=25, GreaterEqual=26, LessEqual=27, NotEqual=28, Equal=29, AndAnd=30, 
		OrOr=31, Not=32, RightShift=33, LeftShift=34, And=35, Or=36, Caret=37, 
		Tilde=38, Assign=39, PlusPlus=40, MinusMinus=41, Dot=42, LeftBracket=43, 
		RightBracket=44, LeftParen=45, RightParen=46, Question=47, Colon=48, Semi=49, 
		Comma=50, LeftBrace=51, RightBrace=52, Quote=53, Identifier=54, DecimalInteger=55, 
		StringConstant=56, Whitespace=57, Newline=58, BlockComment=59, LineComment=60;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"BasicType", "Int", "Bool", "String", "Null", "Void", "True", "False", 
			"If", "Else", "For", "While", "Break", "Continue", "Return", "New", "Class", 
			"This", "Plus", "Minus", "Star", "Divide", "Modulo", "Greater", "Less", 
			"GreaterEqual", "LessEqual", "NotEqual", "Equal", "AndAnd", "OrOr", "Not", 
			"RightShift", "LeftShift", "And", "Or", "Caret", "Tilde", "Assign", "PlusPlus", 
			"MinusMinus", "Dot", "LeftBracket", "RightBracket", "LeftParen", "RightParen", 
			"Question", "Colon", "Semi", "Comma", "LeftBrace", "RightBrace", "Quote", 
			"Identifier", "DecimalInteger", "StringConstant", "Whitespace", "Newline", 
			"BlockComment", "LineComment"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'int'", "'bool'", "'string'", "'null'", "'void'", "'true'", 
			"'false'", "'if'", "'else'", "'for'", "'while'", "'break'", "'continue'", 
			"'return'", "'new'", "'class'", "'this'", "'+'", "'-'", "'*'", "'/'", 
			"'%'", "'>'", "'<'", "'>='", "'<='", "'!='", "'=='", "'&&'", "'||'", 
			"'!'", "'>>'", "'<<'", "'&'", "'|'", "'^'", "'~'", "'='", "'++'", "'--'", 
			"'.'", "'['", "']'", "'('", "')'", "'?'", "':'", "';'", "','", "'{'", 
			"'}'", "'\"'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "BasicType", "Int", "Bool", "String", "Null", "Void", "True", "False", 
			"If", "Else", "For", "While", "Break", "Continue", "Return", "New", "Class", 
			"This", "Plus", "Minus", "Star", "Divide", "Modulo", "Greater", "Less", 
			"GreaterEqual", "LessEqual", "NotEqual", "Equal", "AndAnd", "OrOr", "Not", 
			"RightShift", "LeftShift", "And", "Or", "Caret", "Tilde", "Assign", "PlusPlus", 
			"MinusMinus", "Dot", "LeftBracket", "RightBracket", "LeftParen", "RightParen", 
			"Question", "Colon", "Semi", "Comma", "LeftBrace", "RightBrace", "Quote", 
			"Identifier", "DecimalInteger", "StringConstant", "Whitespace", "Newline", 
			"BlockComment", "LineComment"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MxLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Mx.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2>\u0173\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\3\2\3\2\3\2\3\2\3\2\5\2\u0081\n\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3"+
		"\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13"+
		"\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30"+
		"\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35"+
		"\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3 \3!\3!\3\"\3\"\3\"\3#\3#\3#\3$"+
		"\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3)\3*\3*\3*\3+\3+\3,\3,\3-\3-\3.\3"+
		".\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63\3\64\3\64\3\65\3\65\3"+
		"\66\3\66\3\67\3\67\7\67\u0131\n\67\f\67\16\67\u0134\13\67\38\38\78\u0138"+
		"\n8\f8\168\u013b\138\38\58\u013e\n8\39\39\39\39\79\u0144\n9\f9\169\u0147"+
		"\139\39\39\3:\6:\u014c\n:\r:\16:\u014d\3:\3:\3;\3;\5;\u0154\n;\3;\5;\u0157"+
		"\n;\3;\3;\3<\3<\3<\3<\7<\u015f\n<\f<\16<\u0162\13<\3<\3<\3<\3<\3<\3=\3"+
		"=\3=\3=\7=\u016d\n=\f=\16=\u0170\13=\3=\3=\3\u0160\2>\3\3\5\4\7\5\t\6"+
		"\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24"+
		"\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K"+
		"\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w=y>\3\2\n"+
		"\4\2C\\c|\6\2\62;C\\aac|\3\2\63;\3\2\62;\5\2\"#%]_\u0081\5\2$$^^pp\4\2"+
		"\13\13\"\"\4\2\f\f\17\17\2\u0180\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2"+
		"\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2"+
		"\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2"+
		"\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2"+
		"\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O"+
		"\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2"+
		"\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2"+
		"\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u"+
		"\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\3\u0080\3\2\2\2\5\u0082\3\2\2\2\7\u0086"+
		"\3\2\2\2\t\u008b\3\2\2\2\13\u0092\3\2\2\2\r\u0097\3\2\2\2\17\u009c\3\2"+
		"\2\2\21\u00a1\3\2\2\2\23\u00a7\3\2\2\2\25\u00aa\3\2\2\2\27\u00af\3\2\2"+
		"\2\31\u00b3\3\2\2\2\33\u00b9\3\2\2\2\35\u00bf\3\2\2\2\37\u00c8\3\2\2\2"+
		"!\u00cf\3\2\2\2#\u00d3\3\2\2\2%\u00d9\3\2\2\2\'\u00de\3\2\2\2)\u00e0\3"+
		"\2\2\2+\u00e2\3\2\2\2-\u00e4\3\2\2\2/\u00e6\3\2\2\2\61\u00e8\3\2\2\2\63"+
		"\u00ea\3\2\2\2\65\u00ec\3\2\2\2\67\u00ef\3\2\2\29\u00f2\3\2\2\2;\u00f5"+
		"\3\2\2\2=\u00f8\3\2\2\2?\u00fb\3\2\2\2A\u00fe\3\2\2\2C\u0100\3\2\2\2E"+
		"\u0103\3\2\2\2G\u0106\3\2\2\2I\u0108\3\2\2\2K\u010a\3\2\2\2M\u010c\3\2"+
		"\2\2O\u010e\3\2\2\2Q\u0110\3\2\2\2S\u0113\3\2\2\2U\u0116\3\2\2\2W\u0118"+
		"\3\2\2\2Y\u011a\3\2\2\2[\u011c\3\2\2\2]\u011e\3\2\2\2_\u0120\3\2\2\2a"+
		"\u0122\3\2\2\2c\u0124\3\2\2\2e\u0126\3\2\2\2g\u0128\3\2\2\2i\u012a\3\2"+
		"\2\2k\u012c\3\2\2\2m\u012e\3\2\2\2o\u013d\3\2\2\2q\u013f\3\2\2\2s\u014b"+
		"\3\2\2\2u\u0156\3\2\2\2w\u015a\3\2\2\2y\u0168\3\2\2\2{\u0081\5\5\3\2|"+
		"\u0081\5\7\4\2}\u0081\5\t\5\2~\u0081\5\r\7\2\177\u0081\5m\67\2\u0080{"+
		"\3\2\2\2\u0080|\3\2\2\2\u0080}\3\2\2\2\u0080~\3\2\2\2\u0080\177\3\2\2"+
		"\2\u0081\4\3\2\2\2\u0082\u0083\7k\2\2\u0083\u0084\7p\2\2\u0084\u0085\7"+
		"v\2\2\u0085\6\3\2\2\2\u0086\u0087\7d\2\2\u0087\u0088\7q\2\2\u0088\u0089"+
		"\7q\2\2\u0089\u008a\7n\2\2\u008a\b\3\2\2\2\u008b\u008c\7u\2\2\u008c\u008d"+
		"\7v\2\2\u008d\u008e\7t\2\2\u008e\u008f\7k\2\2\u008f\u0090\7p\2\2\u0090"+
		"\u0091\7i\2\2\u0091\n\3\2\2\2\u0092\u0093\7p\2\2\u0093\u0094\7w\2\2\u0094"+
		"\u0095\7n\2\2\u0095\u0096\7n\2\2\u0096\f\3\2\2\2\u0097\u0098\7x\2\2\u0098"+
		"\u0099\7q\2\2\u0099\u009a\7k\2\2\u009a\u009b\7f\2\2\u009b\16\3\2\2\2\u009c"+
		"\u009d\7v\2\2\u009d\u009e\7t\2\2\u009e\u009f\7w\2\2\u009f\u00a0\7g\2\2"+
		"\u00a0\20\3\2\2\2\u00a1\u00a2\7h\2\2\u00a2\u00a3\7c\2\2\u00a3\u00a4\7"+
		"n\2\2\u00a4\u00a5\7u\2\2\u00a5\u00a6\7g\2\2\u00a6\22\3\2\2\2\u00a7\u00a8"+
		"\7k\2\2\u00a8\u00a9\7h\2\2\u00a9\24\3\2\2\2\u00aa\u00ab\7g\2\2\u00ab\u00ac"+
		"\7n\2\2\u00ac\u00ad\7u\2\2\u00ad\u00ae\7g\2\2\u00ae\26\3\2\2\2\u00af\u00b0"+
		"\7h\2\2\u00b0\u00b1\7q\2\2\u00b1\u00b2\7t\2\2\u00b2\30\3\2\2\2\u00b3\u00b4"+
		"\7y\2\2\u00b4\u00b5\7j\2\2\u00b5\u00b6\7k\2\2\u00b6\u00b7\7n\2\2\u00b7"+
		"\u00b8\7g\2\2\u00b8\32\3\2\2\2\u00b9\u00ba\7d\2\2\u00ba\u00bb\7t\2\2\u00bb"+
		"\u00bc\7g\2\2\u00bc\u00bd\7c\2\2\u00bd\u00be\7m\2\2\u00be\34\3\2\2\2\u00bf"+
		"\u00c0\7e\2\2\u00c0\u00c1\7q\2\2\u00c1\u00c2\7p\2\2\u00c2\u00c3\7v\2\2"+
		"\u00c3\u00c4\7k\2\2\u00c4\u00c5\7p\2\2\u00c5\u00c6\7w\2\2\u00c6\u00c7"+
		"\7g\2\2\u00c7\36\3\2\2\2\u00c8\u00c9\7t\2\2\u00c9\u00ca\7g\2\2\u00ca\u00cb"+
		"\7v\2\2\u00cb\u00cc\7w\2\2\u00cc\u00cd\7t\2\2\u00cd\u00ce\7p\2\2\u00ce"+
		" \3\2\2\2\u00cf\u00d0\7p\2\2\u00d0\u00d1\7g\2\2\u00d1\u00d2\7y\2\2\u00d2"+
		"\"\3\2\2\2\u00d3\u00d4\7e\2\2\u00d4\u00d5\7n\2\2\u00d5\u00d6\7c\2\2\u00d6"+
		"\u00d7\7u\2\2\u00d7\u00d8\7u\2\2\u00d8$\3\2\2\2\u00d9\u00da\7v\2\2\u00da"+
		"\u00db\7j\2\2\u00db\u00dc\7k\2\2\u00dc\u00dd\7u\2\2\u00dd&\3\2\2\2\u00de"+
		"\u00df\7-\2\2\u00df(\3\2\2\2\u00e0\u00e1\7/\2\2\u00e1*\3\2\2\2\u00e2\u00e3"+
		"\7,\2\2\u00e3,\3\2\2\2\u00e4\u00e5\7\61\2\2\u00e5.\3\2\2\2\u00e6\u00e7"+
		"\7\'\2\2\u00e7\60\3\2\2\2\u00e8\u00e9\7@\2\2\u00e9\62\3\2\2\2\u00ea\u00eb"+
		"\7>\2\2\u00eb\64\3\2\2\2\u00ec\u00ed\7@\2\2\u00ed\u00ee\7?\2\2\u00ee\66"+
		"\3\2\2\2\u00ef\u00f0\7>\2\2\u00f0\u00f1\7?\2\2\u00f18\3\2\2\2\u00f2\u00f3"+
		"\7#\2\2\u00f3\u00f4\7?\2\2\u00f4:\3\2\2\2\u00f5\u00f6\7?\2\2\u00f6\u00f7"+
		"\7?\2\2\u00f7<\3\2\2\2\u00f8\u00f9\7(\2\2\u00f9\u00fa\7(\2\2\u00fa>\3"+
		"\2\2\2\u00fb\u00fc\7~\2\2\u00fc\u00fd\7~\2\2\u00fd@\3\2\2\2\u00fe\u00ff"+
		"\7#\2\2\u00ffB\3\2\2\2\u0100\u0101\7@\2\2\u0101\u0102\7@\2\2\u0102D\3"+
		"\2\2\2\u0103\u0104\7>\2\2\u0104\u0105\7>\2\2\u0105F\3\2\2\2\u0106\u0107"+
		"\7(\2\2\u0107H\3\2\2\2\u0108\u0109\7~\2\2\u0109J\3\2\2\2\u010a\u010b\7"+
		"`\2\2\u010bL\3\2\2\2\u010c\u010d\7\u0080\2\2\u010dN\3\2\2\2\u010e\u010f"+
		"\7?\2\2\u010fP\3\2\2\2\u0110\u0111\7-\2\2\u0111\u0112\7-\2\2\u0112R\3"+
		"\2\2\2\u0113\u0114\7/\2\2\u0114\u0115\7/\2\2\u0115T\3\2\2\2\u0116\u0117"+
		"\7\60\2\2\u0117V\3\2\2\2\u0118\u0119\7]\2\2\u0119X\3\2\2\2\u011a\u011b"+
		"\7_\2\2\u011bZ\3\2\2\2\u011c\u011d\7*\2\2\u011d\\\3\2\2\2\u011e\u011f"+
		"\7+\2\2\u011f^\3\2\2\2\u0120\u0121\7A\2\2\u0121`\3\2\2\2\u0122\u0123\7"+
		"<\2\2\u0123b\3\2\2\2\u0124\u0125\7=\2\2\u0125d\3\2\2\2\u0126\u0127\7."+
		"\2\2\u0127f\3\2\2\2\u0128\u0129\7}\2\2\u0129h\3\2\2\2\u012a\u012b\7\177"+
		"\2\2\u012bj\3\2\2\2\u012c\u012d\7$\2\2\u012dl\3\2\2\2\u012e\u0132\t\2"+
		"\2\2\u012f\u0131\t\3\2\2\u0130\u012f\3\2\2\2\u0131\u0134\3\2\2\2\u0132"+
		"\u0130\3\2\2\2\u0132\u0133\3\2\2\2\u0133n\3\2\2\2\u0134\u0132\3\2\2\2"+
		"\u0135\u0139\t\4\2\2\u0136\u0138\t\5\2\2\u0137\u0136\3\2\2\2\u0138\u013b"+
		"\3\2\2\2\u0139\u0137\3\2\2\2\u0139\u013a\3\2\2\2\u013a\u013e\3\2\2\2\u013b"+
		"\u0139\3\2\2\2\u013c\u013e\7\62\2\2\u013d\u0135\3\2\2\2\u013d\u013c\3"+
		"\2\2\2\u013ep\3\2\2\2\u013f\u0145\7$\2\2\u0140\u0144\t\6\2\2\u0141\u0142"+
		"\7^\2\2\u0142\u0144\t\7\2\2\u0143\u0140\3\2\2\2\u0143\u0141\3\2\2\2\u0144"+
		"\u0147\3\2\2\2\u0145\u0143\3\2\2\2\u0145\u0146\3\2\2\2\u0146\u0148\3\2"+
		"\2\2\u0147\u0145\3\2\2\2\u0148\u0149\7$\2\2\u0149r\3\2\2\2\u014a\u014c"+
		"\t\b\2\2\u014b\u014a\3\2\2\2\u014c\u014d\3\2\2\2\u014d\u014b\3\2\2\2\u014d"+
		"\u014e\3\2\2\2\u014e\u014f\3\2\2\2\u014f\u0150\b:\2\2\u0150t\3\2\2\2\u0151"+
		"\u0153\7\17\2\2\u0152\u0154\7\f\2\2\u0153\u0152\3\2\2\2\u0153\u0154\3"+
		"\2\2\2\u0154\u0157\3\2\2\2\u0155\u0157\7\f\2\2\u0156\u0151\3\2\2\2\u0156"+
		"\u0155\3\2\2\2\u0157\u0158\3\2\2\2\u0158\u0159\b;\2\2\u0159v\3\2\2\2\u015a"+
		"\u015b\7\61\2\2\u015b\u015c\7,\2\2\u015c\u0160\3\2\2\2\u015d\u015f\13"+
		"\2\2\2\u015e\u015d\3\2\2\2\u015f\u0162\3\2\2\2\u0160\u0161\3\2\2\2\u0160"+
		"\u015e\3\2\2\2\u0161\u0163\3\2\2\2\u0162\u0160\3\2\2\2\u0163\u0164\7,"+
		"\2\2\u0164\u0165\7\61\2\2\u0165\u0166\3\2\2\2\u0166\u0167\b<\2\2\u0167"+
		"x\3\2\2\2\u0168\u0169\7\61\2\2\u0169\u016a\7\61\2\2\u016a\u016e\3\2\2"+
		"\2\u016b\u016d\n\t\2\2\u016c\u016b\3\2\2\2\u016d\u0170\3\2\2\2\u016e\u016c"+
		"\3\2\2\2\u016e\u016f\3\2\2\2\u016f\u0171\3\2\2\2\u0170\u016e\3\2\2\2\u0171"+
		"\u0172\b=\2\2\u0172z\3\2\2\2\16\2\u0080\u0132\u0139\u013d\u0143\u0145"+
		"\u014d\u0153\u0156\u0160\u016e\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}