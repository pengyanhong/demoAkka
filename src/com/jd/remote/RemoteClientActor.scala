package com.jd.remote

import akka.actor.Actor
import com.jd.common.PerfectNumbers
import akka.actor.Props
import akka.routing.FromConfig
import com.jd.common.Find

class RemoteClient(start: Int, end: Int) extends Actor {
  val agent = context.system.actorOf(Props(new Agent()).withRouter(FromConfig()), "remoteMaster")
//      agent ! Find(start, end, self)
  dispatch

  private def dispatch = {
    val remotePaths = context.system.settings.config.getList("akka.actor.deployment./remoteMaster.target.nodes")
    val count = end - start + 1
    val piece = Math.round(count.toDouble / remotePaths.size()).toInt
    println("%s pieces per node".format(piece))
    var s = start
    while (end >= s) {
      var e = s + piece - 1
      if (e > end)
        e = end
      agent ! Find(s, e, self)
      s = e + 1
    }
    println(agent.path)
  }

  def receive = {
    case PerfectNumbers(list: List[Int]) =>
      println("Found Perfect Numbers:" + list.mkString(","))
      Thread.sleep(2000)
      context.system.shutdown()
  }
}