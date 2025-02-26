package com.cosmos.kafka.event

interface EventType {
    val schema: String
    val eventClass: Class<out DomainEvent>
}