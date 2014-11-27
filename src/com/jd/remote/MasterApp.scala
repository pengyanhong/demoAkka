package com.jd.remote

import com.typesafe.config.ConfigFactory
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSelection.toScala
import akka.actor.ActorSystem
import akka.actor.Props
import akka.kernel.Bootable
import com.jd.common.Master
import com.jd.common.Find

class MasterDaemon extends Bootable {
  val system = ActorSystem("MasterApp", ConfigFactory.load.getConfig("remote"))
  system.actorOf(Props[Master], "master")
  def startup = {}
  def shutdown = {
    system.shutdown()
  }
}

object MasterApp {
  def main(args: Array[String]) {
    new MasterDaemon()
  }
}