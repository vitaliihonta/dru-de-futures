package com.datarootlabs.dru.lecture3

import java.util.concurrent.{Executors, TimeUnit}

import org.openjdk.jmh.annotations._

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration
import scala.util.Random

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Array(Mode.AverageTime))
class SeqVsPar {

  import SeqVsPar._

  @Benchmark
  def seq100(ws: Words): Unit = {
    import ws._
    Await.result(MonadSample.wordsCount(ws.`100-words`), Duration.Inf)
  }

  @Benchmark
  def seq1k(ws: Words): Unit = {
    import ws._
    Await.result(MonadSample.wordsCount(ws.`1k-words`), Duration.Inf)
  }

  @Benchmark
  def seq10000(ws: Words): Unit = {
    import ws._
    Await.result(MonadSample.wordsCount(ws.`10000-words`), Duration.Inf)
  }

  @Benchmark
  def seqMillion(ws: Words): Unit = {
    import ws._
    Await.result(MonadSample.wordsCount(ws.millionWords), Duration.Inf)
  }

  @Benchmark
  def seqBillion(ws: Words): Unit = {
    import ws._
    Await.result(MonadSample.wordsCount(ws.billionWords), Duration.Inf)
  }

  @Benchmark
  def par100(ws: Words): Unit = {
    import ws._
    Await.result(MonadSample.wordsCountPar(`100-words`), Duration.Inf)
  }

  @Benchmark
  def par1k(ws: Words): Unit = {
    import ws._
    Await.result(MonadSample.wordsCountPar(`1k-words`), Duration.Inf)
  }

  @Benchmark
  def par10000(ws: Words): Unit = {
    import ws._
    Await.result(MonadSample.wordsCountPar(`10000-words`), Duration.Inf)
  }

  @Benchmark
  def parMillion(ws: Words): Unit = {
    import ws._
    Await.result(MonadSample.wordsCountPar(millionWords), Duration.Inf)
  }

  @Benchmark
  def parBillion(ws: Words): Unit = {
    import ws._
    Await.result(MonadSample.wordsCountPar(billionWords), Duration.Inf)
  }
}

object SeqVsPar {
  @State(Scope.Benchmark)
  class Words {
    implicit var ec: ExecutionContext = _
    var someWords    : Array[String] = _
    var `100-words`  : List[String]  = _
    var `1k-words`   : List[String]  = _
    var `10000-words`: List[String]  = _
    var millionWords : List[String]  = _
    var billionWords : List[String]  = _

    @Setup
    def setup(): Unit = {
      ec = scala.concurrent.ExecutionContext.fromExecutor(Executors.newWorkStealingPool())
      someWords = Array(
        "scala", "java", "haskell", "idris",
        "int", "long", "string",
        "functor", "monad", "comonad", "sync",
        "lock", "thread", "executor", "executor_service", "execution_context",
        "runnable", "callable", "callback", "hell",
        "future", "promise", "thread_pool",
        "cached_thread_pool", "fixed_thread_pool",
        "fork_join_pool"
      )
      def randomWords(size: Int): List[String] = 1 to size map (_ => someWords(Random.nextInt(someWords.length))) grouped 8 map (_.mkString(" ")) toList

      `100-words` = randomWords(100)
      `1k-words` = randomWords(1000)
      `10000-words` = randomWords(10000)
      millionWords = randomWords(1000000)
      billionWords = randomWords(1000000000)
    }
  }

}
