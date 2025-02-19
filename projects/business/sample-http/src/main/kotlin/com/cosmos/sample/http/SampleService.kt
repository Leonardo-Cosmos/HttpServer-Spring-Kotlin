package com.cosmos.sample.http

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class SampleService @Autowired constructor(
    private val redisTemplate: StringRedisTemplate,
    private val config: SampleConfig,
) {
    private val log: Logger = LoggerFactory.getLogger(SampleService::class.java)

    fun setText(text: String): String {
        val accessCode = UUID.randomUUID().toString()
        redisTemplate.opsForValue()["$KEY_PREFIX_TEXT${accessCode}", text, config.textExpireMinute.toLong()] = TimeUnit.MINUTES

        return accessCode
    }

    fun getText(accessCode: String): String? {
        return redisTemplate.opsForValue()["$KEY_PREFIX_TEXT${accessCode}"]
    }

    companion object Constants {
        const val KEY_PREFIX_TEXT: String = "text:"
    }

}