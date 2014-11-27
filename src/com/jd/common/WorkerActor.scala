package com.jd.common

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.actorRef2Scala

class Worker extends Actor {
  private def sumOfFactors(number: Int) = {
    (1 /: (2 until number)) { (sum, i) => if (number % i == 0) sum + i else sum }
  }

  private def isPerfect(num: Int): Boolean = {
    num == sumOfFactors(num)
  }

  def receive = {
    case Work(num: Int, replyTo: ActorRef) =>
      print("[" + num + "] ")
      replyTo ! Result(num, isPerfect(num))
  }
}