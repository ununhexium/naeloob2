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
    : letter+
    ;

letter
    : UPPER | LOWER
    ;

// Definitions of the elements used on the expressions
EQ          :   '#' ;
SPACE       :   ' ' ;
UPPER       :   ('A'..'Z') ;
LOWER       :   ('a'..'z') ;
// This would not be possible: UPPER and LOWER overlap with the definition of LETTER
// It must be defined in the parser, not the lexer
// LETTER : LOWER | UPPER ;
