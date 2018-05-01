# Naeloob

Naeloob is an expression parser that is kinda backwards and evaluates expressions to `true` or `false`, hense its name.

It's based on a real case where I had to wrap my head around to figure out how to correctly evalute these querries.
It's very easy to have ambiguous expressions with non quoted string and space characters also meaning 'AND'.
The operator priority are reversed, all query keys are on 5 letters except 1 which is on 2, ...
Lots of good stuff there :(

I wrote this repository in order to remember how to write such a parser.

The git repo's history is also important to remember the steps to take.

##Specification

There are several types for keys. They may contain numbers, predefined values or dates.

### Simplest query

`AA#B`

Querry elements where the field AA has the value B.

Can also mean querry elements where the field AA contains value B. This depends on the type of AA.

### Long keyword query

`KAPAR#FloosH`

Where FloosH is a predefined value and may only accept a restricted set of values.
The parser doesn't care about which values exactly, this check is made at evaluation.

Fields name's may have 2 or 5 letters. All of them have 5 letters except 1, because logic and consistency are important... :/

### Querried values may contains spaces

`AA#Hello world`

Is a valid querry where we look for an entity with a key named AA where the value is "Hello world" without the quotes.

### Boolean operations

`AA#A BB#B~CC#C`

Querry elements where AA#A AND BB#B OR CC#C, each expression is like the ones described above.

BUT there is a catch. The OR epxression has to be evaluated BEFORE the AND expression, 
that is, OR has priority over AND, the opposite of the usual boolean operators.

### Negation

`!AA#A`

Negation of AA#A. The negation behaves like regular boolean logic and has a priority higher to that of AND and OR.

### Parenthesis

`[AA#A BB#B]~CC#C`

Do the AND before the OR. Like classic parenthesis would do.

### Funtions

`FF#f[]`

Replace the value f[] by the evaluation of it. For instance, if we use time[], the current time will be used instead of "time[]"

### Dates

`TIMER#01#02#3000`

Querry elements where the field TIMER is set to the first of february of year 3000.

Note that the separator for the date is the same as the separator for the field and value `Field#Value`.

### Internal OR expression

`AA#A~B~C`

Querry elements where the field AA has the value A or the value B or the value C.

This is equivalent to AA#A|AA#B|AA#C

