package com.cosmos.sqlClient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.cosmos.sqlClient",
        "com.cosmos.sample.sql",
    ]
)
class SqlClientApplication

fun main(args: Array<String>) {
    runApplication<SqlClientApplication>(*args)
}
