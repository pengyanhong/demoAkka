package com.jd.remote

import akka.actor.Actor
import com.jd.common.Find
import akka.actor.ActorRef

class Agent extends Actor {
  var master = context.system.actorSelection("/user/master")

  def receive = {
    case Find(start: Int, end: Int, replyTo: ActorRef) if (start > 1 && end >= start) =>
      println(s"receive $start -> $end")
      master ! Find(start, end, sender)
  }
}