// Generated from D:/Study/Compiler\My.g4 by ANTLR 4.9
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MyLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, Typename=3, Identifier=4, DecimalInteger=5, Whitespace=6, 
		Newline=7, BlockComment=8, LineComment=9, LeftParen=10, RightParen=11, 
		LeftBrace=12, RightBrace=13, Plus=14, PlusPlus=15, Minus=16, MinusMinus=17, 
		Star=18, Colon=19, Semi=20, Comma=21, Assign=22, Equal=23, NotEqual=24, 
		Return=25, Continue=26, Break=27;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "Typename", "Identifier", "DecimalInteger", "Whitespace", 
			"Newline", "BlockComment", "LineComment", "LeftParen", "RightParen", 
			"LeftBrace", "RightBrace", "Plus", "PlusPlus", "Minus", "MinusMinus", 
			"Star", "Colon", "Semi", "Comma", "Assign", "Equal", "NotEqual", "Return", 
			"Continue", "Break"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'if'", "'else'", null, null, null, null, null, null, null, "'('", 
			"')'", "'{'", "'}'", "'+'", "'++'", "'-'", "'--'", "'*'", "':'", "';'", 
			"','", "'='", "'=='", "'!='", "'return'", "'continue'", "'break'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, "Typename", "Identifier", "DecimalInteger", "Whitespace", 
			"Newline", "BlockComment", "LineComment", "LeftParen", "RightParen", 
			"LeftBrace", "RightBrace", "Plus", "PlusPlus", "Minus", "MinusMinus", 
			"Star", "Colon", "Semi", "Comma", "Assign", "Equal", "NotEqual", "Return", 
			"Continue", "Break"
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


	public MyLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "My.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\35\u00c2\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4O\n\4\3\5\3\5\7"+
		"\5S\n\5\f\5\16\5V\13\5\3\6\3\6\7\6Z\n\6\f\6\16\6]\13\6\3\6\5\6`\n\6\3"+
		"\7\6\7c\n\7\r\7\16\7d\3\7\3\7\3\b\3\b\5\bk\n\b\3\b\5\bn\n\b\3\b\3\b\3"+
		"\t\3\t\3\t\3\t\7\tv\n\t\f\t\16\ty\13\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n"+
		"\3\n\7\n\u0084\n\n\f\n\16\n\u0087\13\n\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3"+
		"\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\23\3"+
		"\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3"+
		"\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33\3"+
		"\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3w\2\35\3\3\5\4\7\5\t\6\13"+
		"\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'"+
		"\25)\26+\27-\30/\31\61\32\63\33\65\34\67\35\3\2\b\4\2C\\c|\6\2\62;C\\"+
		"aac|\3\2\63;\3\2\62;\4\2\13\13\"\"\4\2\f\f\17\17\2\u00cb\2\3\3\2\2\2\2"+
		"\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2"+
		"\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2"+
		"\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2"+
		"\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2"+
		"\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\39\3\2\2\2\5<\3\2\2\2\7N\3\2\2"+
		"\2\tP\3\2\2\2\13_\3\2\2\2\rb\3\2\2\2\17m\3\2\2\2\21q\3\2\2\2\23\177\3"+
		"\2\2\2\25\u008a\3\2\2\2\27\u008c\3\2\2\2\31\u008e\3\2\2\2\33\u0090\3\2"+
		"\2\2\35\u0092\3\2\2\2\37\u0094\3\2\2\2!\u0097\3\2\2\2#\u0099\3\2\2\2%"+
		"\u009c\3\2\2\2\'\u009e\3\2\2\2)\u00a0\3\2\2\2+\u00a2\3\2\2\2-\u00a4\3"+
		"\2\2\2/\u00a6\3\2\2\2\61\u00a9\3\2\2\2\63\u00ac\3\2\2\2\65\u00b3\3\2\2"+
		"\2\67\u00bc\3\2\2\29:\7k\2\2:;\7h\2\2;\4\3\2\2\2<=\7g\2\2=>\7n\2\2>?\7"+
		"u\2\2?@\7g\2\2@\6\3\2\2\2AB\7k\2\2BC\7p\2\2CO\7v\2\2DE\7d\2\2EF\7q\2\2"+
		"FG\7q\2\2GO\7n\2\2HI\7u\2\2IJ\7v\2\2JK\7t\2\2KL\7k\2\2LM\7p\2\2MO\7i\2"+
		"\2NA\3\2\2\2ND\3\2\2\2NH\3\2\2\2O\b\3\2\2\2PT\t\2\2\2QS\t\3\2\2RQ\3\2"+
		"\2\2SV\3\2\2\2TR\3\2\2\2TU\3\2\2\2U\n\3\2\2\2VT\3\2\2\2W[\t\4\2\2XZ\t"+
		"\5\2\2YX\3\2\2\2Z]\3\2\2\2[Y\3\2\2\2[\\\3\2\2\2\\`\3\2\2\2][\3\2\2\2^"+
		"`\7\62\2\2_W\3\2\2\2_^\3\2\2\2`\f\3\2\2\2ac\t\6\2\2ba\3\2\2\2cd\3\2\2"+
		"\2db\3\2\2\2de\3\2\2\2ef\3\2\2\2fg\b\7\2\2g\16\3\2\2\2hj\7\17\2\2ik\7"+
		"\f\2\2ji\3\2\2\2jk\3\2\2\2kn\3\2\2\2ln\7\f\2\2mh\3\2\2\2ml\3\2\2\2no\3"+
		"\2\2\2op\b\b\2\2p\20\3\2\2\2qr\7\61\2\2rs\7,\2\2sw\3\2\2\2tv\13\2\2\2"+
		"ut\3\2\2\2vy\3\2\2\2wx\3\2\2\2wu\3\2\2\2xz\3\2\2\2yw\3\2\2\2z{\7,\2\2"+
		"{|\7\61\2\2|}\3\2\2\2}~\b\t\2\2~\22\3\2\2\2\177\u0080\7\61\2\2\u0080\u0081"+
		"\7\61\2\2\u0081\u0085\3\2\2\2\u0082\u0084\n\7\2\2\u0083\u0082\3\2\2\2"+
		"\u0084\u0087\3\2\2\2\u0085\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0088"+
		"\3\2\2\2\u0087\u0085\3\2\2\2\u0088\u0089\b\n\2\2\u0089\24\3\2\2\2\u008a"+
		"\u008b\7*\2\2\u008b\26\3\2\2\2\u008c\u008d\7+\2\2\u008d\30\3\2\2\2\u008e"+
		"\u008f\7}\2\2\u008f\32\3\2\2\2\u0090\u0091\7\177\2\2\u0091\34\3\2\2\2"+
		"\u0092\u0093\7-\2\2\u0093\36\3\2\2\2\u0094\u0095\7-\2\2\u0095\u0096\7"+
		"-\2\2\u0096 \3\2\2\2\u0097\u0098\7/\2\2\u0098\"\3\2\2\2\u0099\u009a\7"+
		"/\2\2\u009a\u009b\7/\2\2\u009b$\3\2\2\2\u009c\u009d\7,\2\2\u009d&\3\2"+
		"\2\2\u009e\u009f\7<\2\2\u009f(\3\2\2\2\u00a0\u00a1\7=\2\2\u00a1*\3\2\2"+
		"\2\u00a2\u00a3\7.\2\2\u00a3,\3\2\2\2\u00a4\u00a5\7?\2\2\u00a5.\3\2\2\2"+
		"\u00a6\u00a7\7?\2\2\u00a7\u00a8\7?\2\2\u00a8\60\3\2\2\2\u00a9\u00aa\7"+
		"#\2\2\u00aa\u00ab\7?\2\2\u00ab\62\3\2\2\2\u00ac\u00ad\7t\2\2\u00ad\u00ae"+
		"\7g\2\2\u00ae\u00af\7v\2\2\u00af\u00b0\7w\2\2\u00b0\u00b1\7t\2\2\u00b1"+
		"\u00b2\7p\2\2\u00b2\64\3\2\2\2\u00b3\u00b4\7e\2\2\u00b4\u00b5\7q\2\2\u00b5"+
		"\u00b6\7p\2\2\u00b6\u00b7\7v\2\2\u00b7\u00b8\7k\2\2\u00b8\u00b9\7p\2\2"+
		"\u00b9\u00ba\7w\2\2\u00ba\u00bb\7g\2\2\u00bb\66\3\2\2\2\u00bc\u00bd\7"+
		"d\2\2\u00bd\u00be\7t\2\2\u00be\u00bf\7g\2\2\u00bf\u00c0\7c\2\2\u00c0\u00c1"+
		"\7m\2\2\u00c18\3\2\2\2\f\2NT[_djmw\u0085\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}