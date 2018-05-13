package com.datarootlabs.dru.lecture3

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

object MonadSample {

  def wordsCount(lines: List[String])(implicit ec: ExecutionContext): Future[Map[String, Int]] = for {
    words <- Future {lines.flatMap(_.split(" "))}
    result <- Future {words.groupBy(identity).mapValues(_.size)}
  } yield result


  def wordsCountPar(lines: List[String])(implicit ec: ExecutionContext): Future[Map[String, Int]] = {
    val batches: List[List[String]] = {
      val size = lines.size
      lines.grouped(size / 8).toList
    }

    val resultsFutureList: List[Future[Map[String, Int]]] = batches.map(wordsCount)

    val futureResults: Future[List[Map[String, Int]]] = Future.sequence(resultsFutureList)

    val futureResult: Future[Map[String, Int]] = futureResults.map(_.foldLeft(Map.empty[String, Int]) {
      case (wordsMap, batch) => batch.foldLeft(wordsMap) {
        case (acc, (word, count)) if acc contains word => acc.updated(word, acc(word) + count)
        case (acc2, wordWithCount)                     => acc2 + wordWithCount
      }
    })

    futureResult
  }

  def main(args: Array[String]): Unit = {
    val lines = List(
      "hello world",
      "hello you to",
      "scala rules",
      "callback hell hell hell",
      "hello world",
      "hello you to",
      "scala rules",
      "callback hell hell hell", "hello world",
      "hello you to",
      "scala rules",
      "callback hell hell hell", "hello world",
      "hello you to",
      "scala rules",
      "callback hell hell hell", "hello world",
      "hello you to",
      "scala rules",
      "callback hell hell hell", "hello world",
      "hello you to",
      "scala rules", "scala rules", "scala rules", "scala rules", "scala rules", "scala rules",
      "scala rules",
      "scala rules",
      "callback hell hell hell", "hello world",
      "hello you to",
      "scala rules",
      "callback hell hell hell", "hello world",
      "hello you to",
      "scala rules",
      "callback hell hell hell"
    )

    import scala.concurrent.ExecutionContext.Implicits.global

    val countFuture: Future[Map[String, Int]] = wordsCount(lines)
    val countFuture2: Future[Map[String, Int]] = wordsCountPar(lines)

    println(Await.result(countFuture, 5.seconds))
    println(Await.result(countFuture2, 5.seconds))
  }
}
