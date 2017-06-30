package com.gridpoint.brownbag.akka

import akka.actor.Actor
import akka.actor.PoisonPill
import akka.actor.Props

// An actor can have mutable state, and even mutate it in response to handling a message
// Because it only processes a single message at a time, it is safe
// Other actors or parts of the system cannot access the actors state, only communicate
// via messages.

object Counter {
  case object Count
  def props: Props = Props(new Counter)
}

class Counter extends Actor {
  // mutable state
  var countDown = 100
  context.stop(self)
  def receive = {
    case Counter.Count =>
      if(countDown > 0) {
        countDown = countDown - 1
        println(countDown)
      } else {
        // Stop vs PoisonPill vs Kill
        // self ! Kill would throw an exception which
        // would be handled by the supervision strategy
        self ! PoisonPill
      }
  }
}