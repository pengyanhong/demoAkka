package com.jd.cluster

import scala.concurrent.forkjoin.ThreadLocalRandom
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSelection.toScala
import akka.actor.Address
import akka.actor.RelativeActorPath
import akka.actor.RootActorPath
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.cluster.ClusterEvent.MemberEvent
import akka.cluster.ClusterEvent.MemberRemoved
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.MemberStatus
import com.jd.common.PerfectNumbers
import com.jd.common.Find
import akka.actor.actorRef2Scala
import akka.cluster.ClusterEvent.MemberEvent

class ClusterClient extends Actor {
  val cluster = Cluster(context.system)
  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberEvent])
  override def postStop(): Unit = cluster unsubscribe self

  var nodes = Set.empty[Address]

  val servicePath = "/user/listener"
  val servicePathElements = servicePath match {
    case RelativeActorPath(elements) => elements
    case _ => throw new IllegalArgumentException(
      "servicePath [%s] is not a valid relative actor path" format servicePath)
  }

  def receive = {
    case state: CurrentClusterState =>
      nodes = state.members.collect {
        case m if m.status == MemberStatus.Up => m.address
      }
    case MemberUp(member) =>
      nodes += member.address
    case MemberRemoved(member, _) =>
      nodes -= member.address
    case _: MemberEvent => // ignore
    case PerfectNumbers(list: List[Int]) =>
      println("\nFound Perfect Numbers:" + list.mkString(","))
      cluster.down(self.path.address)
      context.system.shutdown()
    case Find(start: Int, end: Int, resultTo: ActorRef) =>
      println("node size:" + nodes.size)
      nodes.size match {
        case x: Int if x < 1 =>
          Thread.sleep(1000)
          self ! Find(start, end, resultTo)
        case _ =>
          val address = nodes.toIndexedSeq(ThreadLocalRandom.current.nextInt(nodes.size))
          val service = context.actorSelection(RootActorPath(address) / servicePathElements)
          service ! Find(start, end, resultTo)
          println("send to :" + address)
      }
  }
}