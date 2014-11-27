package com.jd.multi
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import scala.concurrent.duration._
import com.jd.cluster.ClusterClient
import com.jd.common.Master
import com.jd.remote.RemoteClient
import com.jd.common.Find
import com.jd.common.Find

object PerfectNumber {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("MasterApp", ConfigFactory.load.getConfig("multiThread"))
    val master = system.actorOf(Props[Master], "master")
    master ! Find(2, 100, master)
    Thread.sleep(2000)
    system.shutdown()
  }

}