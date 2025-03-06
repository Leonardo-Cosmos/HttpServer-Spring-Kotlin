package com.cosmos.sample.sql.controller

import com.cosmos.sample.sql.model.SampleText
import com.cosmos.sample.sql.service.SampleTextService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sample-sql")
class SampleSqlController @Autowired constructor(
    private val sampleTextService: SampleTextService,
) {
    private val log: Logger = LoggerFactory.getLogger(SampleSqlController::class.java)

    @GetMapping("/text")
    fun findAll(): List<SampleText> {
        return sampleTextService.findAll()
    }

    @GetMapping("/text/{key}")
    fun findByKey(@PathVariable key: String): SampleText? {
        return sampleTextService.findByKey(key)
    }

    @PostMapping("/text/{key}")
    fun insert(@PathVariable key: String, @RequestBody sampleText: SampleText) {
        sampleTextService.insert(
            SampleText(
                key = key,
                text = sampleText.text,
            )
        )
    }

    @PutMapping("/text/{key}")
    fun update(@PathVariable key: String, @RequestBody sampleText: SampleText) {
        sampleTextService.update(
            SampleText(
                key = key,
                text = sampleText.text
            )
        )
    }

    @DeleteMapping("/text/{key}")
    fun delete(@PathVariable key: String) {
        sampleTextService.delete(key)
    }

}