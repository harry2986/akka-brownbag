package com.gridpoint.brownbag.akka

import akka.actor.Actor
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

/**
 * Actors should never block when handling messages.
 * If they need to, blocking should be managed.
 * Sync HTTP Client, DB Driver, Email Service etc...
 */
class BlockingActor extends Actor {
  def receive = {
    case i: Int =>
      Thread.sleep(5000) //block for 5 seconds, representing blocking I/O, etc
      println(s"Blocking operation finished: ${i}")
  }
}

class BlockingFutureActor extends Actor {
  /**
   * my-blocking-dispatcher {
      type = Dispatcher
      executor = "thread-pool-executor"
      thread-pool-executor {
        fixed-pool-size = 16
      }
      throughput = 1
    }
    Dispatchers takes care of enqueuing messages into the mailbox of an actor
    as well as scheduling the mailbox for dequeuing one or more messages. 
    They are responsible for deciding which actor is allocated to a thread for
    execution and how/when they are swithced.
   */
  
  implicit val executionContext: ExecutionContext = context.dispatcher
  //implicit val executionContext: ExecutionContext = context.system.dispatchers.lookup("my-blocking-dispatcher")
  
  def receive = {
    case i: Int =>
      println(s"Calling blocking Future: ${i}")
      Future {
        Thread.sleep(5000) //block for 5 seconds
        println(s"Blocking future finished ${i}")
      }
  }
}