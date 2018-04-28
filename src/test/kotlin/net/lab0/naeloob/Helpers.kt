package net.lab0.naeloob

import net.lab0.naeloob.antlr.NaeloobLexer
import net.lab0.naeloob.antlr.NaeloobParser
import org.antlr.v4.runtime.CodePointCharStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.misc.Interval


val ParserRuleContext.sourceCode: String
    get() = this.start.inputStream.getText(
        Interval(this.start.startIndex, this.stop.stopIndex)
    )

val List<ParserRuleContext>.sourceCode: String
    get() = this.first().start.inputStream.getText(
        Interval(this.first().start.startIndex, this.last().stop.stopIndex)
    )

class Parsing(
    val charStream: CodePointCharStream,
    val lexer: NaeloobLexer,
    val parser: NaeloobParser
)
{
    fun getSourceCodeOf(sourceInterval: Interval?): String =
        charStream.getText(sourceInterval)
}
