package com.bot.vkbot.service

import com.bot.vkbot.config.VkConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.web.client.RestTemplate

class VkMessageServiceTest {

    private lateinit var restTemplate: RestTemplate
    private lateinit var vkConfig: VkConfig
    private lateinit var vkMessageService: VkMessageService
    private lateinit var systemTimeService: SystemTimeService

    private val randomId = 123456789L

    @BeforeEach
    fun setUp() {
        systemTimeService = mock(SystemTimeService::class.java)
        restTemplate = mock(RestTemplate::class.java)
        vkConfig = mock(VkConfig::class.java)

        `when`(vkConfig.accessToken).thenReturn("access_token")
        `when`(vkConfig.version).thenReturn("5.199")
        `when`(systemTimeService.currentTimeMillis()).thenReturn(randomId)

        vkMessageService = VkMessageService(restTemplate, vkConfig, systemTimeService)
    }

    @Test
    fun `should send message successfully`() {
        val peerId = 12345
        val text = "Test message"

        val expectedUrl =
            "https://api.vk.com/method/messages.send?peer_id=$peerId&message=Вы сказали: $text&random_id=$randomId&access_token=${vkConfig.accessToken}&v=${vkConfig.version}"

        `when`(restTemplate.postForObject(eq(expectedUrl), eq(null), eq(String::class.java)))
            .thenReturn("OK")

        vkMessageService.sendMessage(peerId, text)

        verify(restTemplate).postForObject(eq(expectedUrl), eq(null), eq(String::class.java))
    }
}
