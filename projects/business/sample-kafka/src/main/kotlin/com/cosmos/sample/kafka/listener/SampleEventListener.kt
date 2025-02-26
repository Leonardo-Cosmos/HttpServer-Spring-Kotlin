package com.cosmos.sample.kafka.listener

import com.cosmos.sample.kafka.event.SampleEventType
import com.cosmos.kafka.event.DomainEvent
import com.cosmos.kafka.listener.AbstractEventListener
import com.fasterxml.jackson.databind.ObjectMapper
import io.cloudevents.CloudEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment

class SampleEventListener(
    mapper: ObjectMapper
) : AbstractEventListener<SampleEventType>(mapper) {

    override val eventTypeClass: Class<SampleEventType> = SampleEventType::class.java

    override val log: Logger = LoggerFactory.getLogger(SampleEventListener::class.java)

    @KafkaListener(
        topics = ["\${events.sample.text.topicName}"],
        groupId = "\${events.sample.text.groupId}",
        containerFactory = "sampleListenerContainerFactory"
    )
    override fun listen(message: CloudEvent, acknowledgment: Acknowledgment) {
        super.listen(message, acknowledgment)
    }

    override fun handleEvent(eventType: SampleEventType, event: DomainEvent) {
        when (eventType) {
            SampleEventType.ADD_TEXT -> {
                log.info("Text added, {}", event)
            }
            SampleEventType.REMOVE_TEXT -> {
                log.info("Text removed, {}", event)
            }
        }
    }
}