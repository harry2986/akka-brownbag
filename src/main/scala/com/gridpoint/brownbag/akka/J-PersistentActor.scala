package com.gridpoint.brownbag.akka

import akka.persistence.PersistentActor

// Event-Sourcing - https://martinfowler.com/eaaDev/EventSourcing.html
// Akka persistence enables stateful actors to persist their internal state.
// Only changes to state are persisted, but not its state
// Append-Only, no mutations, leads to high throughput
// 
// Actor receives a command, and if it is able to apply the command, 
// events are generated. These events are persisted, and if succesful,
// used to update the actors state.

case class AlarmState(status: String) {
  def updated(updatedStatus: String) = this.copy(status = updatedStatus)
}

object AlarmOccurrenceProcessor {
  //Commands  
  trait Command
  case class ProcessAlarmOccurrence(status: String) extends Command

  //Events
  trait Event {
    def status: String
  }
  case class AlarmOccurrenceProcessed(status: String) extends Event
}

// It is also possible to mixin the AtLeastOnceDelivery trait
// to get at least once delivery semantics
class AlarmOccurrenceProcessor extends PersistentActor {
  import AlarmOccurrenceProcessor._

  override def persistenceId = "alarm-occurrence-processor" //unique persistence identifier
  // Internal State
  var state = AlarmState("")

  def updateState(event: Event): Unit = {
    state = state.updated(event.status)
  }

  // Persistent State of the actor can be replayed 
  // to recover the latest state. 
  // This helps to get an audit trail of the state changes
  // Implementing Snapshotting enables recover customization
  def receiveRecover: Receive = {
    case event: Event => updateState(event)
  }

  def receiveCommand: Receive = {
    case ProcessAlarmOccurrence(status) =>
      //def persist[A](event: A)(handler: A => Unit): Unit
      // persistAsync can be used for high throughput. Ordering of
      // events is still preserved, but event callbacks can happen
      // at any time
      persist(AlarmOccurrenceProcessed(status)) { event =>
        updateState(event)
      }
      
    case "print" => println(state)
  }

}
