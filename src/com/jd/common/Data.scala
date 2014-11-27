package com.jd.common

import akka.actor.ActorRef

trait Message
case class Find(start: Int, end: Int, replyTo: ActorRef) extends Message
case class Work(num: Int, replyTo: ActorRef) extends Message
case class Result(num: Int, isPerfect: Boolean) extends Message
case class PerfectNumbers(list: List[Int]) extends Message