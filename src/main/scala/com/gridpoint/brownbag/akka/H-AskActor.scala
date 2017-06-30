package com.gridpoint.brownbag.akka

import akka.actor.Actor
import akka.actor.ActorRef
import akka.pattern.pipe
import akka.pattern.ask
import scala.concurrent.duration.DurationInt
import akka.util.Timeout

// Because async messaging is the only way to communicate with actors, 
// there can still be a need to get specific responses from actors
// The ask pattern allows to send a message to the actor
// and get back a response
object UserClickCounter {
  case object AddClick
  case object GetClicks
  case class Clicks(total: Int)
}

// Increments counter every time it gets an AddClick message
class UserClickCounter extends Actor {
  var counter: Int = 0
  
  def receive: Receive =  {
    case UserClickCounter.AddClick =>
      counter + 1
      
    case UserClickCounter.GetClicks =>
      sender() ! UserClickCounter.Clicks(counter)
  }
}

object StatisticsActor {
  case object PrintStats
}

class StatisticsActor(userClickCounter: ActorRef) extends Actor {
  
  def receive: Receive = {
    case StatisticsActor.PrintStats =>
      implicit val ec = context.system.dispatcher
      implicit val timeout = Timeout(5 seconds)
      
      // Ask is completely non-blocking and returns a future
      // The receiving actor must reply to this message to 
      // complete the future
      val numClicks = userClickCounter ? UserClickCounter.GetClicks     
      numClicks pipeTo self
      
    case UserClickCounter.Clicks(total) =>
      println(total)
  }
}