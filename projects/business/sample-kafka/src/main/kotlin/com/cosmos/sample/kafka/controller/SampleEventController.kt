package com.cosmos.sample.kafka.controller

import com.cosmos.sample.kafka.event.SampleAddTextEvent
import com.cosmos.sample.kafka.event.SampleRemoveTextEvent
import com.cosmos.sample.kafka.publisher.SampleEventPublisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sample-event")
class SampleEventController @Autowired constructor(
    private val sampleEventPublisher: SampleEventPublisher,
) {
    private val log: Logger = LoggerFactory.getLogger(SampleEventController::class.java)

    @PutMapping("/text")
    fun add(@RequestBody addTextEvent: SampleAddTextEvent) {
        sampleEventPublisher.sendAddTextEvent(addTextEvent)
    }

    @DeleteMapping("/text")
    fun remove(@RequestBody removeTextEvent: SampleRemoveTextEvent) {
        sampleEventPublisher.sendRemoveTextEvent(removeTextEvent)
    }

}