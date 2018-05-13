package com.datarootlabs.dru.lecture3

object ByValueVsByName {
  def byValueFunc[A](a: A): Unit = {
    println(s"[#1] a=$a")
    println(s"[#2] a=$a")
    println(s"[#3] a=$a")
  }
  def byNameFunc[A](a: => A): Unit = {
    println(s"[#1] a=$a")
    println(s"[#2] a=$a")
    println(s"[#3] a=$a")
  }

  def getX: Int = {
    val x: Int = 1
    println(s"x=$x")
    x
  }

  def main(args: Array[String]): Unit = {
    byValueFunc(getX)
    byNameFunc(getX)
  }
}
