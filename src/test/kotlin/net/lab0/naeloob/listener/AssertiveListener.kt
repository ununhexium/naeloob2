package net.lab0.naeloob.listener

import org.antlr.v4.runtime.ANTLRErrorListener
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import org.assertj.core.api.Assertions
import java.util.*

class AssertiveListener : ANTLRErrorListener
{
  override fun reportAttemptingFullContext(
      recognizer: Parser?,
      dfa: DFA?,
      startIndex: Int,
      stopIndex: Int,
      conflictingAlts: BitSet?,
      configs: ATNConfigSet?
  )
  {
    throw InvalidQuery("reportAttemptingFullContext")
  }

  override fun syntaxError(
      recognizer: Recognizer<*, *>?,
      offendingSymbol: Any?,
      line: Int,
      charPositionInLine: Int,
      msg: String?,
      e: RecognitionException?
  )
  {
    throw InvalidQuery("Syntax error line $line:$charPositionInLine -> $msg", e)
  }

  override fun reportAmbiguity(
      recognizer: Parser?,
      dfa: DFA?,
      startIndex: Int,
      stopIndex: Int,
      exact: Boolean,
      ambigAlts: BitSet?,
      configs: ATNConfigSet?
  )
  {
    throw InvalidQuery("reportAmbiguity")
  }

  override fun reportContextSensitivity(
      recognizer: Parser?,
      dfa: DFA?,
      startIndex: Int,
      stopIndex: Int,
      prediction: Int,
      configs: ATNConfigSet?
  )
  {
    throw InvalidQuery("reportContextSensitivity")
  }
}