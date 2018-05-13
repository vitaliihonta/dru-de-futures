package com.datarootlabs.dru.lecture3

import scala.language.higherKinds

object ThreadsSample {
  def wordsCount(lines: List[String])(callback: Map[String, Int] => Unit): Unit = {
    var words: List[String] = Nil
    var result: Map[String, Int] = Map.empty

    val getWords: Runnable = () => {
      words = lines.flatMap(_.split(" "))
    }
    val thread1: Thread = new Thread(getWords)

    val getCounts: Runnable = () => {
      thread1.join()
      result = words.groupBy(identity).mapValues(_.size)
    }
    val thread2: Thread = new Thread(getCounts)

    val notify: Runnable = () => {
      thread2.join()
      callback(result)
    }

    val thread3: Thread = new Thread(notify)

    thread1.start()
    thread2.start()
    thread3.start()
    thread3.join()
  }

  def main(args: Array[String]): Unit = {
    val lines = List(
      "hello world",
      "hello you to",
      "scala rules",
      "callback hell hell hell"
    )

    wordsCount(lines)(println)
  }
}
