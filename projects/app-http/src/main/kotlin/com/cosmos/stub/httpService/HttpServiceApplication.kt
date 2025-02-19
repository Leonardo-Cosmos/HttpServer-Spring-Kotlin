package com.cosmos.stub.httpService

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.cosmos.stub.httpService",
        "com.cosmos.stub.sample.http",
    ]
)
class HttpServiceApplication

fun main(args: Array<String>) {
    runApplication<HttpServiceApplication>(*args)
}
