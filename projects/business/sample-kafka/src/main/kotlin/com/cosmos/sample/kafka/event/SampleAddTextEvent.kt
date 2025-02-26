package com.cosmos.sample.kafka.event

import com.cosmos.kafka.event.DomainEvent

data class SampleAddTextEvent(
    val key: String? = null,
    var text: String? = null
) : DomainEvent