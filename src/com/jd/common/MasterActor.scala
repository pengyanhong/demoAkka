package com.jd.common

import scala.collection.mutable.ListBuffer

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.routing.FromConfig

sealed class Helper(count: Int, replyTo: ActorRef) extends Actor {
  val perfectNumbers = new ListBuffer[Int]
  var nrOfResults = 0

  def receive = {
    case Result(num: Int, isPerfect: Boolean) =>
      nrOfResults += 1
      if (isPerfect)
        perfectNumbers += num
      if (nrOfResults == count) {
        replyTo ! PerfectNumbers(perfectNumbers.toList)
        context.stop(self)
      }
  }
}

class Master extends Actor {
//	val workers = context.actorOf(Props(new Worker()), "workerRouter")
//	val workers = context.actorOf(Props(new Worker()).withRouter(RoundRobinRouter(10)), "workerRouter")
  val workers = context.actorOf(Props(new Worker()).withRouter(FromConfig()), "workerRouter")

  def receive = {
    case Find(start: Int, end: Int, replyTo: ActorRef) =>
      require(start > 1 && end >= start)
      val count = end - start + 1
      val helper = context.actorOf(Props(new Helper(count, replyTo)))
      (start to end).foreach(num =>
        workers ! Work(num, helper)
//        workers.tell(ConsistentHashableEnvelope(Work(num,helper), num), helper)  
      )
    case PerfectNumbers(list: List[Int]) =>
      println("\nFound Perfect Numbers:" + list.mkString(","))
  }
}