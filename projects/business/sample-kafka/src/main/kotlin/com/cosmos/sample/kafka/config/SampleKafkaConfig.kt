package com.cosmos.sample.kafka.config

import com.cosmos.kafka.config.DomainEventConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
data class SampleKafkaConfig(
    @Value("\${events.sample.text.bootstrapServers}")
    override val bootstrapServers: String,
    @Value("\${events.sample.text.replicationFactor}")
    override val replicationFactor: Int,
    @Value("\${events.sample.text.clientIdConfig}")
    override val clientIdConfig: String,
    @Value("\${events.sample.text.topicName}")
    override val topicName: String,
    @Value("\${events.sample.text.topicName.dlq}")
    override val dlqTopicName: String,
    @Value("\${events.sample.text.eventSource}")
    override val eventSource: String,
    @Value("\${events.sample.text.eventSubjectPath}")
    override val eventSubjectPath: String,
    @Value("\${events.sample.text.retry.times}")
    override val eventRetryTimes: Int,
    @Value("\${events.sample.text.retry.interval}")
    override val eventRetryInterval: Int,
    @Value("\${events.sample.text.producer.retry.times}")
    override val eventProducerRetryTimes: Int,
) : DomainEventConfig