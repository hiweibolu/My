grammar My;

program: suite EOF;

varDef : Typename Identifier ('=' expression)? ';';

suite : '{' statement* '}';

statement
    : suite
    | varDef
    | 'if' '(' expression ')' statement
        ('else' statement)?
    | 'return' expression? ';'
    | 'break' ';'
    | 'continue' ';'
    | expression ';'
    | ';'
    ;

expression
    : primary
    | expression ('++' | '--')
    | <assoc=right> ('++' | '--') expression
    | expression ('+' | '-') expression
    | expression ('==' | '!=' ) expression
    | <assoc=right> expression '=' expression
    ;

primary
    : '(' expression ')'
    | Identifier
    | literal
    ;

literal
    : DecimalInteger
    ;

Typename
    : 'int'
    | 'bool'
    | 'string'
    ;

Identifier
    : [a-zA-Z] [a-zA-Z_0-9]*
    ;

DecimalInteger
    : [1-9] [0-9]*
    | '0'
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

LeftParen : '(';
RightParen : ')';
LeftBrace : '{';
RightBrace : '}';
Plus : '+';
PlusPlus : '++';
Minus : '-';
MinusMinus : '--';
Star : '*';
Colon : ':';
Semi : ';';
Comma : ',';
Assign : '=';
Equal : '==';
NotEqual : '!=';
Return : 'return';
Continue : 'continue';
Break : 'break';
