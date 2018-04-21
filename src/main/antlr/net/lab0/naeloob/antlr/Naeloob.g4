grammar Naeloob;

@header {
    package net.lab0.naeloob.antlr;
}

parse
    : expr EOF
    ;

expr
    : WORD EQ SENTENCE
    ;


EQ          :   '#' ;
SENTENCE    :   [a-zA-Z]+ ;
LETTER      :   'A'..'Z' ;
WORD        :   LETTER LETTER LETTER LETTER LETTER ;
