package com.gridpoint.brownbag.akka

import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.PersistenceQuery
import akka.actor.ActorSystem
import akka.persistence.query.scaladsl.ReadJournal
import akka.stream.scaladsl.Source
import akka.persistence.query.EventEnvelope
import akka.NotUsed

// The persistence-query API is loosely specified, so that
// journal implementations can expose their own features
// Eg: SQL vs Cassandra
class MyPersistenceQuery(system: ActorSystem) {

  lazy val readJournal =
    PersistenceQuery(system).readJournalFor[JdbcReadJournal](JdbcReadJournal.Identifier)

  // Exposes Akka Streams APIs
  // http://doc.akka.io/api/akka/current/akka/stream/scaladsl/Source.html
  // A Source is a set of stream processing steps that has one open output. 
  // It can comprise any number of internal sources and transformations 
  // that are wired together
  // First type parameter is the type of elements being output by the source
  // Second type parameter is the materialized value of the stream 

  // Returns an infinite stream of events being persisted for the given persistence id
  val events: Source[EventEnvelope, NotUsed] =
    readJournal.eventsByPersistenceId("alarm-occurrence-processor", 0, Long.MaxValue)

  // Returns a finite stream of events that have been persisted for the given persistence id
  val currentEvents: Source[EventEnvelope, NotUsed] =
    readJournal.currentEventsByPersistenceId("alarm-occurrence-processor", 0, Long.MaxValue)

  //Returns an infinite stream of all persistence ids being persisted in the journal 
  val persistenceIds: Source[String, NotUsed] = readJournal.allPersistenceIds()

  //Returns an finite stream of all persistence ids that have been persisted in the journal 
  val currentPersistenceIds: Source[String, NotUsed] = readJournal.currentPersistenceIds()
}