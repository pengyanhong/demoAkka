package com.jd.remote

import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.actor.Props

object PerfectNumber {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("MasterApp", ConfigFactory.load.getConfig("remote"))
    system.actorOf(Props(new RemoteClient(2, 100)),"client")
  }

}