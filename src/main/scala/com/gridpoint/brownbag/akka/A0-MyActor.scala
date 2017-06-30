package com.gridpoint.brownbag.akka

import akka.actor.Actor
import akka.actor.Props
import akka.actor.Actor.Receive
object MyActor {
  def props: Props = Props(new MyActor)
}

// Actors are lightweight, and we can a few dozen
// or a few million in a system.
// No 1-1 relationship between Actor and Thread, and a single
// thread can execute many actors
class MyActor extends Actor {
  // actors are “fundamental units of computation that 
  // embody processing, storage and communication
  // A Universal Modular Actor Formalism for Artificial Intelligence - Carl Hewitt et all (1973)

  //PartialFunction[Any, Unit]
  def receive: Receive = {
    case "Hello" => println("Hello")
  }
}