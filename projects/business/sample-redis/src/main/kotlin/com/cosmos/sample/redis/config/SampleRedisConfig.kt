package com.cosmos.sample.redis.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class SampleRedisConfig(
    @Value("\${sample.access-code-expire.min:5}")
    val textExpireMinute: Int
)