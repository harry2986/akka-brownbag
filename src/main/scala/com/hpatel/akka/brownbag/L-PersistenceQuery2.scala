package com.hpatel.akka.brownbag

import akka.persistence.query.PersistenceQuery
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.actor.ActorSystem
import akka.persistence.journal.WriteEventAdapter
import akka.persistence.journal.Tagged
import akka.stream.scaladsl.Source
import akka.persistence.query.EventEnvelope
import akka.NotUsed

// Some journals can support tagging of events
// when they are being persisted
// This can help when you have multiple (sharded) actors
// with different persistenceIds processing commands
// and persisting events
class MyTaggingEventAdapter extends WriteEventAdapter {
  val tag = "Important"
  val tags = Set(tag)

  override def toJournal(event: Any) = event match {
    case AlarmOccurrenceProcessor.AlarmOccurrenceProcessed(status) =>
      // We can tag specific events, and if needed conditionally
      // Here we only tag these particular types of events
      if (status == "ALARMING" || status == "CLEARED")
        event
      else
        Tagged(event, tags)
  }

  override def manifest(event: Any): String = ""

}

class PersistenceQuery2(system: ActorSystem) {
  lazy val readJournal =
    PersistenceQuery(system).readJournalFor[JdbcReadJournal](JdbcReadJournal.Identifier)

  // An infinite stream of events that are being tagged as Important
  val allImportantEvents: Source[EventEnvelope, NotUsed] = readJournal.currentEventsByTag("Important", 0L)

}