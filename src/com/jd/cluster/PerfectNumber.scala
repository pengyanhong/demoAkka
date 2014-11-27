package com.jd.cluster

import com.jd.common.Find
import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala

object PerfectNumber {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("MasterApp", ConfigFactory.load.getConfig("cluster"))
    val client = system.actorOf(Props[ClusterClient], "listener")
    Thread.sleep(2000)
    client ! Find(2, 100, client)
  }

}