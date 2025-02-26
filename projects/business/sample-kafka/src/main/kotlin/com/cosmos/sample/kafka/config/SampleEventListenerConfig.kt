package com.cosmos.sample.kafka.config

import com.cosmos.kafka.config.KafkaConfigurationConstants
import com.cosmos.kafka.event.CustomizedCloudEventDeserializer
import com.cosmos.sample.kafka.listener.SampleEventListener
import com.fasterxml.jackson.databind.ObjectMapper
import io.cloudevents.core.message.Encoding
import io.cloudevents.jackson.JsonFormat
import io.cloudevents.kafka.CloudEventSerializer
import io.cloudevents.kafka.PartitionKeyExtensionInterceptor
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.CommonErrorHandler
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.util.backoff.FixedBackOff

@EnableKafka
@Configuration
class SampleEventListenerConfig {

    @Bean(name = ["sampleConsumerFactory"])
    fun sampleConsumerFactory(kafkaConfig: SampleKafkaConfig): ConsumerFactory<String, Any> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = ErrorHandlingDeserializer::class.java
        props[ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS] = CustomizedCloudEventDeserializer::class.java
        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = KafkaConfigurationConstants.ENABLED_FALSE
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = KafkaConfigurationConstants.OFFSET_RESET_EARLIEST
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaConfig.bootstrapServers
        props[ConsumerConfig.CLIENT_ID_CONFIG] = kafkaConfig.clientIdConfig
        return DefaultKafkaConsumerFactory(props)
    }

    @Bean(name = ["sampleDlqProducerFactory"])
    fun sampleDlqProducerFactory(kafkaConfig: SampleKafkaConfig): ProducerFactory<Any, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaConfig.bootstrapServers
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = CloudEventSerializer::class.java
        props[CloudEventSerializer.ENCODING_CONFIG] = Encoding.STRUCTURED
        props[CloudEventSerializer.EVENT_FORMAT_CONFIG] = JsonFormat.CONTENT_TYPE
        props[ProducerConfig.INTERCEPTOR_CLASSES_CONFIG] = PartitionKeyExtensionInterceptor::class.java.canonicalName
        props[ProducerConfig.ACKS_CONFIG] = KafkaConfigurationConstants.PUBLISHER_CONFIG_ACKS_ALL
        props[ProducerConfig.RETRIES_CONFIG] = kafkaConfig.eventProducerRetryTimes
        props[ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION] =
            KafkaConfigurationConstants.PUBLISHER_REQUESTS_PER_CONNECTION_ONE
        props[ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG] = false
        return DefaultKafkaProducerFactory(props)
    }

    @Bean(name = ["sampleDlqKafkaTemplate"])
    fun sampleDlqKafkaTemplate(
        @Qualifier("sampleDlqProducerFactory") producerFactory: ProducerFactory<Any, Any>
    ): KafkaTemplate<Any, Any> {
        return KafkaTemplate(producerFactory)
    }

    @Bean(name = ["sampleDeadLetterPublishingRecoverer"])
    fun SampleDeadLetterPublishingRecoverer(
        @Qualifier("sampleDlqKafkaTemplate") kafkaTemplate: KafkaTemplate<Any, Any>
    ): DeadLetterPublishingRecoverer {
        return DeadLetterPublishingRecoverer(kafkaTemplate)
    }

    @Bean(name = ["sampleCommonErrorHandler"])
    fun sampleCommonErrorHandler(
        @Qualifier("sampleDeadLetterPublishingRecoverer") deadLetterPublishingRecoverer: DeadLetterPublishingRecoverer,
        kafkaConfig: SampleKafkaConfig
    ): CommonErrorHandler {
        return DefaultErrorHandler(
            deadLetterPublishingRecoverer,
            FixedBackOff(kafkaConfig.eventRetryInterval.toLong(), kafkaConfig.eventRetryTimes.toLong())
        )
    }

    @Bean(name = ["sampleListenerContainerFactory"])
    fun sampleListenerContainerFactory(
        kafkaConfig: SampleKafkaConfig,
        @Qualifier("sampleCommonErrorHandler") commonErrorHandler: CommonErrorHandler,
        @Qualifier("sampleConsumerFactory") consumerFactory: ConsumerFactory<String, Any>
    ): ConcurrentKafkaListenerContainerFactory<String, Any> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = consumerFactory
        factory.setCommonErrorHandler(commonErrorHandler)
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        return factory
    }

    @Bean
    fun sampleEventListener(
        mapper: ObjectMapper
    ): SampleEventListener {
        return SampleEventListener(mapper)
    }

}