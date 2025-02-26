package com.cosmos.sample.kafka.config

import com.cosmos.kafka.config.KafkaConfigurationConstants
import com.cosmos.sample.kafka.publisher.SampleEventPublisher
import com.fasterxml.jackson.databind.ObjectMapper
import io.cloudevents.CloudEvent
import io.cloudevents.core.message.Encoding
import io.cloudevents.jackson.JsonFormat
import io.cloudevents.kafka.CloudEventSerializer
import io.cloudevents.kafka.PartitionKeyExtensionInterceptor
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class SampleEventPublisherConfig {

    @Bean(name = ["sampleKafkaProducer"])
    fun sampleKafkaProducer(kafkaConfig: SampleKafkaConfig): KafkaProducer<String, CloudEvent> {
        val props = Properties()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaConfig.bootstrapServers
        props[ProducerConfig.CLIENT_ID_CONFIG] = kafkaConfig.clientIdConfig
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = CloudEventSerializer::class.java
        props[CloudEventSerializer.ENCODING_CONFIG] = Encoding.STRUCTURED
        props[CloudEventSerializer.EVENT_FORMAT_CONFIG] = JsonFormat.CONTENT_TYPE
        props[ProducerConfig.INTERCEPTOR_CLASSES_CONFIG] = PartitionKeyExtensionInterceptor::class.java.canonicalName
        props[ProducerConfig.ACKS_CONFIG] = KafkaConfigurationConstants.PUBLISHER_CONFIG_ACKS_ALL
        props[ProducerConfig.RETRIES_CONFIG] = kafkaConfig.eventProducerRetryTimes
        props[ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION] = KafkaConfigurationConstants.PUBLISHER_REQUESTS_PER_CONNECTION_ONE
        props[ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG] = false
        return KafkaProducer<String, CloudEvent>(props)
    }

    @Bean(name = ["sampleEventPublisher"])
    fun sampleEventPublisher(
        kafkaConfig: SampleKafkaConfig,
        @Qualifier("sampleKafkaProducer") producer: KafkaProducer<String, CloudEvent>,
        mapper: ObjectMapper
    ): SampleEventPublisher {
        return SampleEventPublisher(kafkaConfig, producer, mapper)
    }

}