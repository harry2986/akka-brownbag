package com.gridpoint.brownbag.akka

import akka.actor.Actor

// Actors are lightweight, and we can a few dozen
// or a few million in a system.
// No 1-1 relationship between Actor and Thread, and a single
// thread can execute many actors
class MyActor extends Actor {

  def receive: PartialFunction[Any, Unit] = {
    case "Hello" => println("Hello")
  }
}