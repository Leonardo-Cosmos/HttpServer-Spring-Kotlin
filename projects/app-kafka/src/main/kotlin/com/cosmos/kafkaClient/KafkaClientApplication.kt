package com.cosmos.kafkaClient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.cosmos.kafkaClient",
        "com.cosmos.sample.kafka",
    ]
)
class KafkaClientApplication

fun main(args: Array<String>) {
    runApplication<KafkaClientApplication>(*args)
}
