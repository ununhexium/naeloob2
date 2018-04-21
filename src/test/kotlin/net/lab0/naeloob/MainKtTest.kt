package net.lab0.naeloob

import net.lab0.naeloob.antlr.NaeloobLexer
import net.lab0.naeloob.antlr.NaeloobParser
import net.lab0.naeloob.listener.AssertiveListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.jupiter.api.Test


internal class MainKtTest
{
  companion object
  {
    val listener = AssertiveListener()
  }

  @Test
  fun queryWeather()
  {
    // given a query
    val query = "KAPAR#FLOOSH"
    
    // and its parser
    val parser = parse(query)

    // it must not fail at evaluation
    parser.expr()
  }

  private fun parse(query: String): NaeloobParser
  {
    val lexer = NaeloobLexer(CharStreams.fromString(query))
    lexer.addErrorListener(listener)
    val tokens = CommonTokenStream(lexer)
    val parser = NaeloobParser(tokens)
    parser.addErrorListener(listener)
    return parser
  }
}
