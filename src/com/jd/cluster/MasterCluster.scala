package com.jd.cluster

import com.typesafe.config.ConfigFactory
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.cluster.Cluster
import com.jd.common.Find
import com.jd.common.Master

class ClusterListener extends Actor {
  val master=context.system.actorOf(Props[Master],"master")
  
  def receive = {
    case Find(start: Int, end: Int, resultTo: ActorRef) =>
      master ! Find(start, end, sender)
  }
}

object MasterCluster {
  def main(args: Array[String]) {
    val system = ActorSystem("MasterApp", ConfigFactory.load.getConfig("cluster"))
    val clusterListener = system.actorOf(Props(new ClusterListener()), "listener")
    Cluster(system).registerOnMemberUp(clusterListener)
  }
}