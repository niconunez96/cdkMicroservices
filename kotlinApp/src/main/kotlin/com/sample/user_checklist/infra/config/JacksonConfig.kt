package com.sample.user_checklist.infra.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {

    @Bean
    fun jsonMapper(): ObjectMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
}
