package com.gridpoint.brownbag.akka

import akka.actor.ActorSystem

object SupervisiorExampleApp extends App {
  val system: ActorSystem = ActorSystem("SupervisorExample")
  val supervisor = system.actorOf(RandomGeneratorSupervisor.props)
  
}