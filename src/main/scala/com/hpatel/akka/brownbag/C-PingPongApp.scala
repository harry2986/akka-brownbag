package com.hpatel.akka.brownbag

import akka.actor.ActorSystem
import akka.actor.ActorRef


object PingPongApp extends App {
 
  // Actors need an ActorSystem, they cannot live on their own
  //
  val system: ActorSystem = ActorSystem("PingPong")
  
  // You can create root actors by using system.actorOf. 
  // These should be created sparingly, because ideally 
  // you want to have a hierarchy of actors. This helps 
  // a lot with supervision especially
  // Creating the actor does not return an instance, but an ActorRef instead
  // It is immutable, can be serialized, and hence actors can be location transparent
  val pinger: ActorRef = system.actorOf(Pinger.props, "Pinger") 
  val ponger: ActorRef = system.actorOf(Pinger.props, "Ponger")
  // Always name the actors
  
  ponger.tell(Pinger.Ping, pinger)
}


//Benchmark
//http://letitcrash.com/post/20397701710/50-million-messages-per-second-on-a-single