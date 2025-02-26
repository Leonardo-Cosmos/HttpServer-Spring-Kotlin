package com.cosmos.kafka.event

import io.cloudevents.CloudEvent
import io.cloudevents.core.builder.CloudEventBuilder
import io.cloudevents.core.message.MessageReader
import io.cloudevents.kafka.KafkaMessageFactory
import io.cloudevents.rw.CloudEventRWException
import org.apache.kafka.common.header.Headers
import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI
import java.util.UUID

class CustomizedCloudEventDeserializer : Deserializer<CloudEvent> {

    private val log: Logger = LoggerFactory.getLogger(CustomizedCloudEventDeserializer::class.java)

    override fun configure(configs: Map<String?, *>?, isKey: Boolean) {
        super.configure(configs, isKey)
    }

    override fun deserialize(topic: String?, data: ByteArray?): CloudEvent {
        throw UnsupportedOperationException()
    }

    override fun deserialize(topic: String?, headers: Headers?, data: ByteArray?): CloudEvent {
        val reader: MessageReader
        try {
            reader = KafkaMessageFactory.createReader(headers!!, data!!)
        } catch (e: CloudEventRWException) {
            log.error(
                "cloudEvent deserialize exception,exception type is {},message is {}",
                e.kind,
                e.message,
                e
            )
            return CloudEventBuilder.v1()
                .withId(UUID.randomUUID().toString())
                .withSource(URI.create(KafkaEventConstants.CLOUD_EVENT_INVALID_SOURCE))
                .withType(KafkaEventConstants.CLOUD_EVENT_INVALID_TYPE)
                .withData(data)
                .build()
        }

        val message =  reader.toEvent()
        log.info("Received message {}", message)
        return message
    }

    override fun close() {
        super.close()
    }

}