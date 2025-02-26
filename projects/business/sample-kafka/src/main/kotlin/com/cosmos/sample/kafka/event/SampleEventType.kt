package com.cosmos.sample.kafka.event

import com.cosmos.kafka.event.DomainEvent
import com.cosmos.kafka.event.EventType

enum class SampleEventType(
    override val schema: String,
    override val eventClass: Class<out DomainEvent>,
) : EventType {
    ADD_TEXT("com.cosmos.sample.text.add", SampleAddTextEvent::class.java),
    REMOVE_TEXT("com.cosmos.sample.text.remove", SampleRemoveTextEvent::class.java),
}