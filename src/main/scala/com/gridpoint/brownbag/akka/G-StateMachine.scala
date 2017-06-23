package com.gridpoint.brownbag.akka

import akka.actor.Actor
import akka.actor.Stash
import akka.actor.FSM

object StateMachine {
  case object Idle
  case object Active
  case object Failure
  case object ProcessMe
}

class StateMachine extends Actor with Stash {
  import StateMachine._
  
  val initial: PartialFunction[Any, Unit] = {
    case Idle =>
      println("Idle")
 
    case Active => 
      println(s"Becoming Active")
      context.become(active)
      unstashAll()
      
    case _ =>
      stash()
  }
  
  val active: PartialFunction[Any,Unit] = {
    case ProcessMe => 
      println("Processed")
      
    case Failure =>
      println(s"Becoming Active")
      context.unbecome() // context.become(initial)
      unstashAll()
    
    case _ => stash()
      
  }
  
  def receive = initial
}

// Akka provides an FSM Trait which you can use when
// there is a need to model multiple behaviours in the
// same actor