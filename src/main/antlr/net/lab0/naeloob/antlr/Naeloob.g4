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
    | singleExpr                                    #singleExpression
    ;

and
 : SPACE
 ;

or
 : OR
 ;

singleExpr
    : word EQ sentence
    ;

word
    : UPPER UPPER | UPPER UPPER UPPER UPPER UPPER
    ;

sentence
    : letter
    | letter clause* letter
    ;

clause
    : UPPER | LOWER | SPACE
    ;

letter
    : UPPER | LOWER
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
UPPER       :   ('A'..'Z') ;
LOWER       :   ('a'..'z') ;
