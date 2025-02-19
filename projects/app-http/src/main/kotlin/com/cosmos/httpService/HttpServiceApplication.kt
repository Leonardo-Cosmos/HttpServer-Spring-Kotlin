package com.cosmos.httpService

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.cosmos.httpService",
        "com.cosmos.sample.http",
    ]
)
class HttpServiceApplication

fun main(args: Array<String>) {
    runApplication<HttpServiceApplication>(*args)
}
