package com.datarootlabs.dru.lecture3

import java.util.concurrent.{Future => JFuture, _}

object ExecutorServiceSample {
  def wordsCount(lines: List[String]): JFuture[Map[String, Int]] = {
    val service: ExecutorService = Executors.newFixedThreadPool(4)

    val getWords: Callable[List[String]] = () => {
      lines.flatMap(_.split(" "))
    }

    val wordsFuture: JFuture[List[String]] = service.submit(getWords)

    val getCounts: Callable[Map[String, Int]] = () => {
      val words = wordsFuture.get()
      words.groupBy(identity).mapValues(_.size)
    }

    val f2: JFuture[Map[String, Int]] = service.submit(getCounts)

    f2
  }

  def main(args: Array[String]): Unit = {
    val lines = List(
      "hello world",
      "hello you to",
      "scala rules",
      "callback hell hell hell"
    )

    val countFuture: JFuture[Map[String, Int]] = wordsCount(lines)

    println(countFuture.get())
  }
}
