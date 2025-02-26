package com.cosmos.sample.kafka.publisher

import com.cosmos.sample.kafka.event.SampleAddTextEvent
import com.cosmos.sample.kafka.event.SampleEventType
import com.cosmos.sample.kafka.event.SampleRemoveTextEvent
import com.cosmos.kafka.config.DomainEventConfig
import com.cosmos.kafka.publisher.AbstractEventPublisher
import com.fasterxml.jackson.databind.ObjectMapper
import io.cloudevents.CloudEvent
import org.apache.kafka.clients.producer.KafkaProducer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SampleEventPublisher(
    config: DomainEventConfig,
    producer: KafkaProducer<String, CloudEvent>,
    mapper: ObjectMapper
) : AbstractEventPublisher<SampleEventType>(config, producer, mapper) {

    override val eventTypeClass: Class<SampleEventType> = SampleEventType::class.java

    override val log: Logger = LoggerFactory.getLogger(SampleEventPublisher::class.java)

    fun sendAddTextEvent(addTextEvent: SampleAddTextEvent) {
        addTextEvent.key?.let {
            this.publish(addTextEvent.key, addTextEvent)
        }
    }

    fun sendRemoveTextEvent(removeTextEvent: SampleRemoveTextEvent) {
        removeTextEvent.key?.let {
            this.publish(removeTextEvent.key, removeTextEvent)
        }
    }

}