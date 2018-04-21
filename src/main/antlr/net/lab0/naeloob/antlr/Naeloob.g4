grammar Naeloob;

// For java:
@header {
    package net.lab0.naeloob.antlr;
}

// The root of the expression
parse
    : blank* expr blank* EOF
    ;

expr
    : LPAREN expr RPAREN                            #parenExpression
    | NOT expr                                      #notExpression
    | left=expr op=or right=expr                    #orExpression
    | left=expr op=and right=expr                   #andExpression
    | single                                        #singleExpression
    ;

and
 : SPACE
 ;

or
 : OR
 ;

function
    : letter+ LPAREN RPAREN
    ;

dateAdd
    : function ('+'|'-') DIGIT+
    ;

single
    : word EQ sentence
    ;

word
    : UPPER UPPER | UPPER UPPER UPPER UPPER UPPER
    ;

date
    : DIGIT DIGIT EQ DIGIT DIGIT EQ DIGIT DIGIT DIGIT DIGIT
    ;

sentence
    : letter
    | dateAdd
    | function
    | date
    | letter clause* letter
    ;

clause
    : letter | SPACE
    ;

letter
    : UPPER | LOWER | POINT | ASTERISK
    ;

blank
    : SPACE
    ;

// Definitions of the elements used on the expressions
EQ          :   '#' ;
OR          :   '~' ;
NOT         :   '!' ;
SPACE       :   ' ' ;
LPAREN      :   '[' ;
RPAREN      :   ']' ;
POINT       :   '.' ;
ASTERISK    :   '*' ;
DIGIT       :   ('0'..'9') ;
UPPER       :   ('A'..'Z') ;
LOWER       :   ('a'..'z') ;
