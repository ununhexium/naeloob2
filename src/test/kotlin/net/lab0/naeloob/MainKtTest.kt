package net.lab0.naeloob

import net.lab0.naeloob.antlr.NaeloobLexer
import net.lab0.naeloob.antlr.NaeloobParser
import net.lab0.naeloob.listener.AssertiveListener
import net.lab0.naeloob.listener.InvalidQuery
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows


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
    val parser = prepare(query).parser

    // it must not fail at evaluation
    val expr = parser.singleExpr()

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
    val parser = prepare(query).parser

    // it must not fail at evaluation
    val expr = parser.singleExpr()

    assertThat(expr.word().text).isEqualTo("KAPAR")
    assertThat(expr.sentence().text).isEqualTo("FloosH")
  }

  @Test
  fun `Ending spaces in 'AA#Hello out there    ' are not part of the sentence`()
  {
    // given a query
    val query = "AA#Hello out there    "

    // and its parser
    val prepare = prepare(query)
    val parser = prepare.parser

    // it must not fail at evaluation
    val expr = parser.singleExpr()

    assertThat(expr.sentence().sourceCodeWithChildren)
        .isEqualTo("Hello out there")
  }

  @TestFactory
  fun validSingleExprQueries(): List<DynamicTest>
  {
    val queries = listOf(
        "CABRA#Whatever",
        "AB#SomethingElse",
        "GR#Nope",
        // the query may or may not have any number of spaces around it
        "AA#A",
        "AA#A ",
        " AA#A",
        " AA#A ",
        "     AA#A   ",
        // the query may have spaces in the sentence
        "AA#Hello out there",
        "AA#Hello   out    there",
        "AA#Hello out there    ",
        // the query may be have disjunctions
        "AA#A~BB#B",
        "AA#A~BB#B~CC#C",
        ('A'..'Z').joinToString(separator = "~") { "$it$it#$it" },
        // the query may have conjunctions
        "AA#A BB#B",
        "AA#A BB#B CC#C",
        ('A'..'Z').joinToString(separator = " ") { "$it$it#$it" }
    )

    return queries.map {
      DynamicTest.dynamicTest(dot(it), { prepare(it).parser.parse() })
    }
  }

  @TestFactory
  fun invalidQueries(): List<DynamicTest>
  {
    val queries = listOf(
        // this in invalid because the sentence can't start with space
        "AA# Hello out there"
    )

    return queries.map {
      DynamicTest.dynamicTest(dot(it), { assertThrows<InvalidQuery> { prepare(it).parser.parse() } })
    }
  }

  /**
   * Add upper dot diacritic over invisible chars to see it in tests and prevent trimming
   */
  private fun dot(it: String) = it.replace(" ", " \u02D9")

  private fun prepare(query: String): Parsing
  {
    val charStream = CharStreams.fromString(query)
    val lexer = NaeloobLexer(charStream)
    lexer.addErrorListener(listener)
    val tokens = CommonTokenStream(lexer)
    val parser = NaeloobParser(tokens)
    parser.addErrorListener(listener)
    return Parsing(charStream, lexer, parser)
  }
}

