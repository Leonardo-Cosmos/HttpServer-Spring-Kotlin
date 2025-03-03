package com.cosmos.sample.redis.controller

import com.cosmos.sample.redis.service.SampleRedisService
import com.cosmos.sample.redis.models.TextRequest
import com.cosmos.sample.redis.models.TextResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/sample-redis")
class SampleRedisController @Autowired constructor(
    private val sampleRedisService: SampleRedisService,
) {
    private val log: Logger = LoggerFactory.getLogger(SampleRedisController::class.java)

    @PostMapping("/text")
    fun save(@RequestBody req: TextRequest): String {
        log.info("Set text request: {}", req)

        val accessCode = sampleRedisService.setText(req.text)
        log.info("Set text access code: {}", accessCode)
        return accessCode;
    }

    @GetMapping("/text")
    fun read(@RequestParam(ACCESS_CODE) accessCode: String?): TextResponse {
        Assert.notNull(accessCode, "Access code is required")
        log.info("Get text, access code: {}", accessCode)

        val text = sampleRedisService.getText(accessCode!!)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Invalid access code: $accessCode"
            )
        val res = TextResponse(
            text = text
        )
        log.info("Get text response: {}", res)
        return res
    }

    companion object Constants {
        const val ACCESS_CODE: String = "code"
    }

}