package com.cosmos.sample.sql.config

import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Configuration

@Configuration
@MapperScan("com.cosmos.sample.sql")
class MapperConfig {
}