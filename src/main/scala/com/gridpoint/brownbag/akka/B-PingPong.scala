package com.gridpoint.brownbag.akka

import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging
import akka.actor.ActorRef

object Pinger {
  // Immutable Messages are the only way to communicate with actors  
  case object Ping
  case object Pong
  
  // Common pattern to pass along the reply address
  case class PingWithReplyTo(replyTo: ActorRef)
  case class PongWithReplyTo(replyTo: ActorRef)
  
  def props: Props = Props(new Pinger)
}

/**
 * Actor can reply to messages, drop messages or schedule messages.
 * Actor can also have state
 */
class Pinger extends Actor {
  
  def receive: PartialFunction[Any,Unit] = {
    case Pinger.Ping =>      
      // ! is another and more common way of sending messages
      // def !(message: Any)(implicit sender: ActorRef): Unit
      println("Ping")
      sender ! Pinger.Pong
          
    case Pinger.Pong =>      
      println("Pong")
      sender ! Pinger.Ping
      
    case Pinger.PingWithReplyTo(replyTo) =>
      println("PingWithReplyTo")
      replyTo ! Pinger.PingWithReplyTo(self) // self reference 
  }
}

