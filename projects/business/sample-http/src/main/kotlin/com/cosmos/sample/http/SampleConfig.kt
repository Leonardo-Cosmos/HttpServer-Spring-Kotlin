package com.cosmos.sample.http

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class SampleConfig(
    @Value("\${sample.access-code-expire.min:5}")
    val textExpireMinute: Int
)