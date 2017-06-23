package com.gridpoint.brownbag.akka

import akka.actor.ActorSystem
import akka.actor.ActorRef


object PingPongApp extends App {
 
  // Actors need an ActorSystem, they cannot live on their own
  val system: ActorSystem = ActorSystem("PingPong")
  
  // You can create root actors by using system.actorOf. 
  // These should be created sparingly, because ideally 
  // you want to have a hierarchy of actors. This helps 
  // a lot with supervision especially
  val pinger: ActorRef = system.actorOf(Pinger.props, "Pinger") // does not return an instance, but an address instead
  val ponger: ActorRef = system.actorOf(Pinger.props, "Ponger")
  // Always name the actors
  
  ponger.tell(Pinger.Ping, ponger)
}


//Benchmark
//http://letitcrash.com/post/20397701710/50-million-messages-per-second-on-a-single