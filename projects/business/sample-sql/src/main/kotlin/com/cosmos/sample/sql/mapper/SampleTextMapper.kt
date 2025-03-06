package com.cosmos.sample.sql.mapper

import com.cosmos.sample.sql.entity.SampleTextEntity
import org.apache.ibatis.annotations.Mapper

@Mapper
interface SampleTextMapper {

    fun findAll(): List<SampleTextEntity>

    fun findByKey(key: String): SampleTextEntity?

    fun insert(sampleText: SampleTextEntity)

    fun update(sampleText: SampleTextEntity)

    fun delete(key: String)
}