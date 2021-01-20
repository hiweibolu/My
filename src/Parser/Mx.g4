grammar Mx;

program : (classDef | varDef | funcDef)* EOF;

varDef : type Identifier ('=' expression)? ';';
classDef : Class Identifier '{' (varDef | funcDef)* '}' ';';
funcDef : type? Identifier '(' funcParamList? ')' suite;

funcParamList : funcParam (',' funcParam)*;
funcParam : type Identifier;

suite : '{' statement* '}';

index : '[' expression? ']';

statement
    : suite #block
    | varDef #varDefStmt
    | If '(' expression ')' trueStmt = statement
        (Else falseStmt = statement)? #ifStmt
    | While '(' expression ')' statement #whileStmt
    | For
        '(' forInit = expression?
        ';' forCondition = expression?
        ';' forIncr = expression?
        ')' statement #forStmt
    | Return expression? ';' #returnStmt
    | Break ';' #breakStmt
    | Continue ';' #continueStmt
    | expression ';' #pureExprStmt
    | ';' #emptyStmt
    ;

expression
    : expression '(' (expression (',' expression)* )? ')' # callExpr
    | '(' expression ')' #atomExpr
    | New basicType index+ #newExpr
    | New basicType ('(' ')')? #newExpr
    | expression op = ('++' | '--') #suffixExpr
    | expression '[' expression ']' #indexExpr
    | expression '.' Identifier #memberExpr
    | <assoc = right> op = ('++' | '--') expression #prefixExpr
    | <assoc = right> op = ('+' | '-') expression #prefixExpr
    | <assoc = right> op = ('!' | '~') expression #prefixExpr
    | expression op = ('*' | '/' | '%') expression #binaryExpr
    | expression op = ('+' | '-') expression #binaryExpr
    | expression op = ('<<' | '>>') expression #binaryExpr
    | expression op = ('<' | '>' | '<=' | '>=') expression #binaryExpr
    | expression op = ('==' | '!=') expression #binaryExpr
    | expression op = '&' expression #binaryExpr
    | expression op = '^' expression #binaryExpr
    | expression op = '|' expression #binaryExpr
    | expression op = '&&' expression #binaryExpr
    | expression op = '||' expression #binaryExpr
    | <assoc = right> expression op = '=' expression #assignExpr
    | This #thisExpr
    | Identifier #idExpr
    | literal #literalExpr
    ;

literal
    : True
    | False
    | DecimalInteger
    | StringConstant
    | Null
    ;

type
    : basicType ('[' ']')*
    ;

basicType
    : Int
    | Bool
    | String
    | Void
    | Identifier
    ;

Int : 'int';
Bool : 'bool';
String : 'string';
Null : 'null';
Void : 'void';
True : 'true';
False : 'false';
If : 'if';
Else : 'else';
For : 'for';
While : 'while';
Break : 'break';
Continue : 'continue';
Return : 'return';
New : 'new';
Class : 'class';
This : 'this';

Plus : '+';
Minus : '-';
Star : '*';
Divide : '/';
Modulo : '%';

Greater : '>';
Less : '<';
GreaterEqual : '>=';
LessEqual : '<=';
NotEqual : '!=';
Equal : '==';

AndAnd : '&&';
OrOr : '||';
Not : '!';

RightShift : '>>';
LeftShift : '<<';
And : '&';
Or : '|';
Caret : '^';
Tilde : '~';

Assign : '=';

PlusPlus : '++';
MinusMinus : '--';

Dot : '.';

LeftBracket : '[';
RightBracket : ']';

LeftParen : '(';
RightParen : ')';

Question : '?';
Colon : ':';
Semi : ';';
Comma : ',';
LeftBrace : '{';
RightBrace : '}';

Quote : '"';

Identifier
    : [a-zA-Z] [a-zA-Z_0-9]*
    ;

DecimalInteger
    : [1-9] [0-9]*
    | '0'
    ;

StringConstant
    : '"' ([\u0020-\u0021\u0023-\u005b\u005d-\u007f] | '\\' [n\\"])* '"'
    ;

Whitespace
    :   [ \t]+
        -> skip
    ;

Newline
    :   (   '\r' '\n'?
        |   '\n'
        )
        -> skip
    ;

BlockComment
    :   '/*' .*? '*/'
        -> skip
    ;

LineComment
    :   '//' ~[\r\n]*
        -> skip
    ;