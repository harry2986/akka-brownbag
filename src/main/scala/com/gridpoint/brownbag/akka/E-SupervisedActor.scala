package com.gridpoint.brownbag.akka

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.SupervisorStrategy
import akka.actor.OneForOneStrategy

object RandomGenerator {
  case class Generate(replyTo: ActorRef)
  case class RandomDouble(value: Double)

  def props: Props = Props(new RandomGenerator)
  def buggyProps: Props = Props(new BuggyRandomGenerator)
}

class RandomGenerator extends Actor {
  import util.Random

  def receive = {
    case RandomGenerator.Generate(replyTo) =>
      replyTo ! RandomGenerator.RandomDouble(Random.nextDouble())
  }
}

class BuggyRandomGenerator extends Actor {
  import util.Random

  def receive = {
    case RandomGenerator.Generate(replyTo) =>
      if (Random.nextBoolean()) throw new RuntimeException("FAILED!!!")
      replyTo ! RandomGenerator.RandomDouble(Random.nextDouble())
  }
}



// Supervisor Actor
object RandomGeneratorSupervisor  {
  def props: Props = Props(new RandomGeneratorSupervisor)
}

class RandomGeneratorSupervisor extends Actor {
  val generator = context.actorOf(RandomGenerator.props)
  //val generator = context.actorOf(RandomGenerator.buggyProps)
  
  // Function Calls or code in the Actor' body is run 
  // when the Actor gets instantiated
  getRandom
  
  def receive = {
    case RandomGenerator.RandomDouble(value) => println(value)
  }
  
  private def getRandom = {
    1 to 10 map { i => 
      generator ! RandomGenerator.Generate(self) 
    }
  }

  override def supervisorStrategy: SupervisorStrategy = {
    // You can chose AllForOneStrategy or OneForOneStrategy
    OneForOneStrategy() {
      // Resume/Restart/Stop/Escalate
      case _ => SupervisorStrategy.Resume
    }
  }
}