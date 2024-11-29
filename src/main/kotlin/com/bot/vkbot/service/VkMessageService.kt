package com.bot.vkbot.service

import com.bot.vkbot.config.VkConfig
import com.bot.vkbot.getLogger
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * Service for sending messages via the VK API.
 *
 * @property restTemplate RestTemplate used to execute HTTP requests.
 * @property vkConfig Configuration for interacting with the VK API.
 */
@Service
class VkMessageService(
    private val restTemplate: RestTemplate,
    private val vkConfig: VkConfig,
    private val systemTimeService: SystemTimeService
) {

    private val prefix = "Вы сказали: "
    private val messageUrl = "https://api.vk.com/method/messages.send"

    private val logger = getLogger<VkMessageService>()

    /**
     * Sends a message to a user via the VK API.
     *
     * @param peerId The ID of the recipient.
     * @param text The message text.
     */
    fun sendMessage(peerId: Int, text: String) {
        val params = mapOf(
            "peer_id" to peerId.toString(),
            "message" to prefix + text,
            "random_id" to systemTimeService.currentTimeMillis().toString(),
            "access_token" to vkConfig.accessToken,
            "v" to vkConfig.version
        )

        val query = params.entries.joinToString("&") { "${it.key}=${it.value}" }
        val finalUrl = "$messageUrl?$query"

        try {
            val response = restTemplate.postForObject(finalUrl, null, String::class.java)
            logger.debug("RESPONSE: $response")
        } catch (ex: Exception) {
            logger.error("Failed to send message to peer=$peerId", ex)
        }
    }
}
