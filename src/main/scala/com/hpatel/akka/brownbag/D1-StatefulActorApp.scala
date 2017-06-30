package com.hpatel.akka.brownbag

import akka.actor.ActorSystem

object StatefulActorApp extends App {
  val system = ActorSystem("StatefulActorApp")
  val counter = system.actorOf(Counter.props)
  
  1 to 200 map { i =>
    counter ! Counter.Count
  }
}