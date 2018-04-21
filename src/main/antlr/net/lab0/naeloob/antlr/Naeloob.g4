grammar Naeloob;

// For java:
@header {
    package net.lab0.naeloob.antlr;
}

// The root of the expression
parse
    : expr EOF
    ;

blank
    : SPACE
    ;

expr
    : blank* word EQ sentence blank*
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

// Definitions of the elements used on the expressions
EQ          :   '#' ;
SPACE       :   ' ' ;
UPPER       :   ('A'..'Z') ;
LOWER       :   ('a'..'z') ;
