package com.jd.simple

import scala.concurrent.duration._
import scala.collection.mutable.ListBuffer

object PerfectNumberSimple {
  def sumOfFactors(number: Int) = {
    (1 /: (2 until number)) { (sum, i) => if (number % i == 0) sum + i else sum }
  }

  def isPerfect(num: Int): Boolean = {
    num == sumOfFactors(num)
  }

  def findPerfectNumbers(start: Int, end: Int) = {
    require(start > 1 && end >= start)
    val perfectNumbers = new ListBuffer[Int]
    (start to end).foreach(num => if (isPerfect(num)) perfectNumbers += num)
    perfectNumbers.toList
  }

  def main(args: Array[String]): Unit = {
    val list = findPerfectNumbers(2, 100)
    println("Found Perfect Numbers:" + list.mkString(","))
  }
}