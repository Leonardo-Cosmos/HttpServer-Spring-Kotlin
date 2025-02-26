package com.cosmos.sample.kafka.event

import com.cosmos.kafka.event.DomainEvent

data class SampleRemoveTextEvent(
    val key: String? = null
) : DomainEvent