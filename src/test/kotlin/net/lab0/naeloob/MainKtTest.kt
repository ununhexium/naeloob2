package net.lab0.naeloob

import net.lab0.naeloob.antlr.NaeloobLexer
import net.lab0.naeloob.antlr.NaeloobParser
import net.lab0.naeloob.listener.AssertiveListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory


internal class MainKtTest
{
  companion object
  {
    val listener = AssertiveListener()
  }

  @Test
  fun query101()
  {
    // given a query
    val query = "AB#C"

    // and its parser
    val parser = parse(query)

    // it must not fail at evaluation
    val expr = parser.expr()

    assertThat(expr.word().text).isEqualTo("AB")
    assertThat(expr.sentence().text).isEqualTo("C")
    // this one is constant, no need to test it
    assertThat(expr.EQ().text).isEqualTo("#")
  }

  @Test
  fun queryWeather()
  {
    // given a query
    val query = "KAPAR#FloosH"

    // and its parser
    val parser = parse(query)

    // it must not fail at evaluation
    val expr = parser.expr()

    assertThat(expr.word().text).isEqualTo("KAPAR")
    assertThat(expr.sentence().text).isEqualTo("FloosH")
  }

  @TestFactory
  fun validQueries(): List<DynamicTest>
  {
    val queries = listOf(
        "CABRA#Whatever",
        "AB#SomethingElse",
        "GR#Nope"
    )

    return queries.map {
      DynamicTest.dynamicTest(it, { parse(it).parse() })
    }
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
