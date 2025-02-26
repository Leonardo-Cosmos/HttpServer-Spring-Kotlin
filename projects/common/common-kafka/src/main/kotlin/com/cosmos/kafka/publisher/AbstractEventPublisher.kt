package com.cosmos.kafka.publisher

import com.cosmos.kafka.config.DomainEventConfig
import com.cosmos.kafka.event.DomainEvent
import com.cosmos.kafka.event.EventType
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.cloudevents.CloudEvent
import io.cloudevents.core.builder.CloudEventBuilder
import io.cloudevents.kafka.PartitionKeyExtensionInterceptor
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpServerErrorException
import java.net.URI
import java.util.*
import java.util.concurrent.ExecutionException

abstract class AbstractEventPublisher<E> protected constructor(
    private val config: DomainEventConfig,
    private val producer: KafkaProducer<String, CloudEvent>,
    private val mapper: ObjectMapper,
) where E : Enum<E>, E : EventType {

    protected abstract val eventTypeClass: Class<E>

    protected abstract val log: Logger

    fun publish(subject: String, data: DomainEvent): RecordMetadata {
        try {
            return publishEvent(subject, data)
        } catch (e: JsonProcessingException) {
            log.error("Publish event failed with json processing error, exception {}", e.message, e)
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "data parse json exception")
        } catch (e: ExecutionException) {
            log.error("Publish event failed with execution error, exception {}", e.message, e)
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "producer exception")
        } catch (e: InterruptedException) {
            log.error("Publish event failed with interrupt error, exception {}", e.message, e)
            /* Clean up whatever needs to be handled before interrupting  */
            Thread.currentThread().interrupt()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "producer interrupted exception")
        }
    }

    @Throws(
        JsonProcessingException::class,
        ExecutionException::class,
        InterruptedException::class
    )
    private fun publishEvent(subject: String, data: DomainEvent): RecordMetadata {
        val eventId = UUID.randomUUID().toString()
        val event: CloudEvent = CloudEventBuilder.v1()
            .withSource(URI.create(config.eventSource))
            .newBuilder()
            .withId(eventId)
            .withType(getEventType(data.javaClass)?.schema)
            .withSubject(subject)
            .withExtension(
                PartitionKeyExtensionInterceptor.PARTITION_KEY_EXTENSION,
                "${subject}-${config.eventSubjectPath}"
            )
            .withData(mapper.writeValueAsBytes(data))
            .build()

        log.info(
            "Sending event with ID: {}, to: {}, data {}, content {}",
            eventId, config.topicName, data, event
        )
        val metadata: RecordMetadata = producer
            .send(ProducerRecord(config.topicName, eventId, event))
            .get()
        producer.flush()
        log.info("Event is sent successfully, ID: {}", eventId)
        return metadata
    }

    private fun getEventType(eventClass: Class<out DomainEvent>): E? {
        return eventClass?.let {
            for (eventType in eventTypeClass.enumConstants) {
                if (eventType.eventClass == eventClass) {
                    return eventType
                }
            }
            return null
        } ?: run {
            null
        }
    }
}