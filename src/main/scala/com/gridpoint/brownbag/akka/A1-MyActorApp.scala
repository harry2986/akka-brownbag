package com.gridpoint.brownbag.akka

import akka.actor.ActorSystem
import akka.actor.ActorRef

object MyActorApp extends App {
  val system: ActorSystem = ActorSystem("PingPong")
  
  val myActor: ActorRef = system.actorOf(MyActor.props)
  myActor ! "Hello"
  
  system.shutdown()
  
}