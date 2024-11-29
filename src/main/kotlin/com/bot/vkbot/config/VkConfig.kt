package com.bot.vkbot.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "vk")
data class VkConfig(
    var version: String = "",
    var accessToken: String = "",
    var confirmationCode: String = ""
)
