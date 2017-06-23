package com.gridpoint.brownbag.akka

import akka.actor.Actor
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

/**
 * Actors should never block when handling messages.
 * If they need to, blocking should be managed.
 * 
 */
class BlockingActor extends Actor {
  def receive = {
    case i: Int =>
      Thread.sleep(5000) //block for 5 seconds, representing blocking I/O, etc
      println(s"Blocking operation finished: ${i}")
  }
}

class BlockingFutureActor extends Actor {
  implicit val executionContext: ExecutionContext = context.dispatcher
  /**
   * my-blocking-dispatcher {
      type = Dispatcher
      executor = "thread-pool-executor"
      thread-pool-executor {
        fixed-pool-size = 16
      }
      throughput = 1
    }
   */
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