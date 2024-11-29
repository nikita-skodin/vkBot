package com.bot.vkbot.controllers

import com.bot.vkbot.config.VkConfig
import com.bot.vkbot.getLogger
import com.bot.vkbot.service.VkMessageService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for handling incoming callbacks from the VK server.
 *
 * @property vkConfig Configuration for interacting with the VK API.
 * @property vkMessageService Service for sending messages to users.
 */
@RestController
@RequestMapping("/callback")
class VkCallbackController(
    private val vkConfig: VkConfig,
    private val vkMessageService: VkMessageService
) {

    private val logger = getLogger<VkCallbackController>()

    /**
     * Handles incoming requests from the VK server.
     *
     * @param body The request body containing event information.
     * @return The confirmation code for the VK server or "ok" after processing.
     */
    @PostMapping
    fun handleCallback(@RequestBody body: Map<String, Any>): String {
        val type = body["type"] as? String ?: run {
            logger.warn("Request type is empty")
            return "ok"
        }
        logger.debug("REQUEST: {}", body)
        return when (type) {
            "confirmation" -> vkConfig.confirmationCode
            "message_new" -> {
                val message = (body["object"] as Map<*, *>)["message"] as Map<*, *>
                val peerId = message["peer_id"] as Int
                val text = message["text"] as String
                vkMessageService.sendMessage(peerId, text)
                "ok"
            }

            else -> {
                logger.warn("Unexpected type $type")
                "ok"
            }
        }
    }
}
