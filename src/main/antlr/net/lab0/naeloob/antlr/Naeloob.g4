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
    | left=expr op=OR right=expr                    #orExpression
    | left=expr op=SPACE right=expr                 #andExpression
    | single                                        #singleExpression
    | internalOr                                    #internalOrExpression
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

internalOr
    : word EQ sentence (OR sentence)+
    ;

word
    : UPPER UPPER | UPPER UPPER UPPER UPPER UPPER
    ;

date
    : DIGIT DIGIT '#' DIGIT DIGIT '#' DIGIT DIGIT DIGIT DIGIT
    ;

sentence
    : dateAdd
    | function
    | date
    | letter clause* letter
    | letter
    ;

clause
    : letter | SPACE
    ;

letter
    : UPPER | LOWER | DIGIT | SAFE | '.' | '*'
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
DIGIT       :   ('0'..'9') ;
UPPER       :   ('A'..'Z') ;
LOWER       :   ('a'..'z') ;
SAFE        :   '_' ;