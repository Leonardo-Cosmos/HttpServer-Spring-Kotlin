package com.cosmos.kafka.config

interface DomainEventConfig {
    val bootstrapServers: String
    val replicationFactor: Int
    val clientIdConfig: String
    val topicName: String
    val dlqTopicName: String
    val eventSource: String
    val eventSubjectPath: String
    val eventRetryTimes: Int
    val eventRetryInterval: Int
    val eventProducerRetryTimes: Int
}