grammar Naeloob;

// For java:
@header {
    package net.lab0.naeloob.antlr;
}

// The root of the expression
parse
    : expr EOF
    ;

expr
    : blank* singleExpr blank*
    | blank* or blank*
    | blank* and blank*
    ;

singleExpr
    : word EQ sentence
    ;

or
    : singleExpr (OR singleExpr)+
    ;

and
    : singleExpr (SPACE singleExpr)+
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
SPACE       :   ' ' ;
UPPER       :   ('A'..'Z') ;
LOWER       :   ('a'..'z') ;
