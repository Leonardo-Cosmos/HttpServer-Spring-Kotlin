package com.cosmos.sample.sql.service

import com.cosmos.sample.sql.entity.SampleTextEntity
import com.cosmos.sample.sql.mapper.SampleTextMapper
import com.cosmos.sample.sql.model.SampleText
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SampleTextService @Autowired constructor(
    private val sampleTextMapper: SampleTextMapper
) {
    private val log: Logger = LoggerFactory.getLogger(SampleTextService::class.java)

    fun findAll(): List<SampleText> {
        return sampleTextMapper.findAll()
            .map(::fromEntity)
    }

    fun findByKey(key: String): SampleText? {
        return sampleTextMapper.findByKey(key)?.let(::fromEntity)
    }

    fun insert(sampleText: SampleText) {
        sampleTextMapper.insert(toEntity(sampleText))
    }

    fun update(sampleText: SampleText) {
        sampleTextMapper.update(toEntity(sampleText))
    }

    fun delete(key: String) {
        sampleTextMapper.delete(key)
    }

    private fun toEntity(sampleText: SampleText): SampleTextEntity {
        return SampleTextEntity(
            key = sampleText.key,
            text = sampleText.text,
        )
    }

    private fun fromEntity(sampleTextEntity: SampleTextEntity): SampleText {
        return SampleText(
            key = sampleTextEntity.key,
            text = sampleTextEntity.text,
        )
    }

}