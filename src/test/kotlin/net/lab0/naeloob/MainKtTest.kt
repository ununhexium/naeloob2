package net.lab0.naeloob

import net.lab0.naeloob.antlr.NaeloobLexer
import net.lab0.naeloob.antlr.NaeloobParser
import net.lab0.naeloob.antlr.NaeloobParser.AndExpressionContext
import net.lab0.naeloob.antlr.NaeloobParser.DateAddContext
import net.lab0.naeloob.antlr.NaeloobParser.DateContext
import net.lab0.naeloob.antlr.NaeloobParser.InternalOrContext
import net.lab0.naeloob.antlr.NaeloobParser.NotExpressionContext
import net.lab0.naeloob.antlr.NaeloobParser.OrExpressionContext
import net.lab0.naeloob.antlr.NaeloobParser.ParenExpressionContext
import net.lab0.naeloob.antlr.NaeloobParser.SingleContext
import net.lab0.naeloob.antlr.NaeloobParser.SingleExpressionContext
import net.lab0.naeloob.listener.AssertiveListener
import net.lab0.naeloob.listener.InvalidQuery
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import java.util.*


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
    val expr = parser.single()

    assertThat(expr.word().text).isEqualTo("AB")
    assertThat(expr.sentence().sourceCode).isEqualTo("C")
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
    val expr = parser.single()

    assertThat(expr.word().text).isEqualTo("KAPAR")
    assertThat(expr.sentence().sourceCode).isEqualTo("FloosH")
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
    val expr = parser.single()

    assertThat(expr.sentence().sourceCode)
        .isEqualTo("Hello out there")
  }

  @Test
  fun `OR has priority over AND`()
  {
    val query = "AA#A BB#B~CC#C"
    val expr = prepare(query).parser.parse().expr()

    assertThat(expr.sourceCode).isEqualTo("AA#A BB#B~CC#C")

    assertThat(expr.getRuleContext(SingleExpressionContext::class.java, 0).sourceCode)
        .isEqualTo("AA#A")
    assertThat(expr.getRuleContext(OrExpressionContext::class.java, 0).right.sourceCode)
        .isEqualTo("CC#C")
    assertThat(expr.getRuleContext(OrExpressionContext::class.java, 0).left.sourceCode)
        .isEqualTo("BB#B")
  }

  @Test
  fun `an expression can be negated`()
  {
    val query = "!AA#A"
    val not = prepare(query).parser.parse().expr()
    assertThat(not.sourceCode)
        .isEqualTo(query)

    val single = not.getRuleContext(SingleExpressionContext::class.java, 0)
    assertThat(single.sourceCode)
        .isEqualTo(query.drop(1))
  }

  @Test
  fun `not has priority over and`()
  {
    val query = "!AA#A BB#B"

    val and = prepare(query).parser.parse().expr()

    val not = and.getRuleContext(NotExpressionContext::class.java, 0)
    assertThat(not.sourceCode).isEqualTo("!AA#A")
  }

  @Test
  fun `not has priority over or`()
  {
    val query = "!AA#A~BB#B"

    val or = prepare(query).parser.parse().expr()

    val not = or.getRuleContext(NotExpressionContext::class.java, 0)
    assertThat(not.sourceCode).isEqualTo("!AA#A")
  }

  @Test
  fun `paren override priority between AND and OR`()
  {
    val query = "[AA#A BB#B]~CC#C"

    val or = prepare(query).parser.parse().expr()
    assertThat(or).isInstanceOf(OrExpressionContext::class.java)

    val paren = or.getRuleContext(ParenExpressionContext::class.java, 0)
    assertThat(paren.sourceCode).isEqualTo("[AA#A BB#B]")

    val and = paren.getRuleContext(AndExpressionContext::class.java, 0)
    assertThat(and.sourceCode).isEqualTo("AA#A BB#B")
  }

  @Test
  fun `parent override priority between NOT and OR`()
  {
    val query = "![AA#A BB#B]"

    val not = prepare(query).parser.parse().expr()
    assertThat(not).isInstanceOf(NotExpressionContext::class.java)

    val paren = not.getRuleContext(ParenExpressionContext::class.java, 0)
    assertThat(paren.sourceCode).isEqualTo("[AA#A BB#B]")

    val and = paren.getRuleContext(AndExpressionContext::class.java, 0)
    assertThat(and.sourceCode).isEqualTo("AA#A BB#B")
  }

  @Test
  fun `supports functions parsing`()
  {
    val query = "TIMER#Time[]"

    val single = prepare(query).parser.single()
    assertThat(single).isInstanceOf(SingleContext::class.java)

    val function = single.sentence()
    assertThat(function.sourceCode)
        .isEqualTo("Time[]")
  }

  @Test
  fun `supports dates`()
  {
    val query = "TIMER#33#22#1111"

    val single = prepare(query).parser.single()
    assertThat(single).isInstanceOf(SingleContext::class.java)

    val function = single.sentence().date()
    assertThat(function)
        .isInstanceOf(DateContext::class.java)
    assertThat(function.sourceCode)
        .isEqualTo("33#22#1111")
  }

  @Test
  fun `supports dates offset`()
  {
    val query = "TIMER#Time[]+0"

    val single = prepare(query).parser.single()
    assertThat(single).isInstanceOf(SingleContext::class.java)

    val dateAdd = single.sentence().dateAdd()
    assertThat(dateAdd)
        .isInstanceOf(DateAddContext::class.java)

    assertThat(dateAdd.sourceCode)
        .isEqualTo("Time[]+0")

    assertThat(dateAdd.function().sourceCode).isEqualTo("Time[]")
  }

  @Test
  fun `supports internal or`()
  {
    val query = "AA#A~B~C"

    val expr = prepare(query).parser.expr()
    val orSentences = expr.getRuleContext(InternalOrContext::class.java, 0)
    assertThat(orSentences).isInstanceOf(InternalOrContext::class.java)
  }

  @TestFactory
  fun validSingleExprQueries(): List<DynamicTest>
  {
    val random = Random()
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
        ('A'..'Z').joinToString(separator = " ") { "$it$it#$it" },
        /**
         * the query may combine AND and OR
         * If such is the case, OR has priority over and
         */
        "AA#A BB#B",
        "AA#A BB#B~CC#C",
        ('A'..'Z')
            .map { "$it$it#$it" }
            .reduce({ a, b ->
              a + "~ ".toList()[random.nextInt(2)] + b
            }),
        // support point wildcard
        "AA#.",
        "AA#a.",
        "AA#.a",
        "AA#a.a",
        "AA#..",
        "AA#...",
        // support star wildcard
        "AA#*",
        "AA#a*",
        "AA#*a",
        "AA#a*a",
        "AA#**",
        "AA#***",
        // time operations
        "TIMER#Date[]+0",
        "TIMER#Date[]-0",
        "TIMER#Date[]+116",
        "TIMER#Date[]-116",
        // internal OR with combinations
        "AA#A~B~C~BB#B~C~D CC#C~D~E",
        // the content can also have digits
        "AA#123",
        "AA#123~123"
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
      DynamicTest.dynamicTest(dot(it), {
        assertThrows<InvalidQuery> { prepare(it).parser.parse() }
      })
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

