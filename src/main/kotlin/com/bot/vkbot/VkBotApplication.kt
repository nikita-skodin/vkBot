package com.bot.vkbot

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@SpringBootApplication
class VkBotApplication {
    @Bean
    fun restTemplate() = RestTemplate()

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("public")
            .packagesToScan("com.bot.vkbot")
            .build()
    }

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("API Documentation")
                    .version("1.0")
                    .description("VK Bot API")
                    .contact(Contact().name("Support Team").email("skodin36@gmail.com"))
            )
    }
}

fun main(args: Array<String>) {
    runApplication<VkBotApplication>(*args)
}

inline fun <reified T> getLogger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}
