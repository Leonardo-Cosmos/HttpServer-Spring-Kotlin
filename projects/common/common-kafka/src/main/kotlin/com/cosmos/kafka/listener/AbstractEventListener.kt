package com.cosmos.kafka.listener

import com.cosmos.kafka.event.DomainEvent
import com.cosmos.kafka.event.EventType
import com.fasterxml.jackson.databind.ObjectMapper
import io.cloudevents.CloudEvent
import io.cloudevents.core.CloudEventUtils
import io.cloudevents.jackson.PojoCloudEventDataMapper
import org.slf4j.Logger
import org.springframework.kafka.support.Acknowledgment

abstract class AbstractEventListener<E> protected constructor(
    private val mapper: ObjectMapper,
) where E : Enum<E>, E : EventType {

    protected abstract val eventTypeClass: Class<E>

    protected abstract val log: Logger

    protected abstract fun handleEvent(eventType: E, event: DomainEvent)

    open fun listen(message: CloudEvent, acknowledgment: Acknowledgment) {
        val eventData = parseMessage(message)
        eventData ?: run {
            log.error("Cannot find matched event type, message ID: {}, type: {}", message.id, message.type)
            acknowledgment.acknowledge()
            return
        }
        val (eventType, event) = eventData
        handleEvent(eventType, event)
    }

    private fun parseMessage(message: CloudEvent): Pair<E, DomainEvent>? {
        val eventType: E? = getEventType(message.type)
        return eventType?.let {
            try {
                val rPojoCloudEventData =
                    CloudEventUtils.mapData(message, PojoCloudEventDataMapper.from(mapper, eventType.eventClass))
                return Pair(eventType, rPojoCloudEventData!!.value)
            } catch (e: Exception) {
                log.warn(
                    "Parse message to event failed, ID: {}, type: {}, error: {}",
                    message.id, message.type, e.message, e
                )
                return null
            }
        } ?: run {
            null
        }
    }

    private fun getEventType(schema: String?): E? {
        return schema?.let {
            for (eventType in eventTypeClass.enumConstants) {
                if (eventType.schema == schema) {
                    return eventType
                }
            }
            return null
        } ?: run {
            null
        }
    }
}