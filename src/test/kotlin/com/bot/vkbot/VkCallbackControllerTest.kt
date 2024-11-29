package com.bot.vkbot

import com.bot.vkbot.config.VkConfig
import com.bot.vkbot.controllers.VkCallbackController
import com.bot.vkbot.service.VkMessageService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import kotlin.test.assertEquals

class VkCallbackControllerTest {

    private lateinit var vkConfig: VkConfig
    private lateinit var vkMessageService: VkMessageService
    private lateinit var controller: VkCallbackController
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        vkConfig = mock(VkConfig::class.java)
        vkMessageService = mock(VkMessageService::class.java)
        
        `when`(vkConfig.confirmationCode).thenReturn("confirmation_code")

        controller = VkCallbackController(vkConfig, vkMessageService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    @Test
    fun `should return confirmation code when type is confirmation`() {
        val requestBody = """
            {
                "type": "confirmation"
            }
        """.trimIndent()

        val result: MvcResult = mockMvc.perform(
            MockMvcRequestBuilders.post("/callback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andReturn()

        assertEquals("confirmation_code", result.response.contentAsString)
    }

    @Test
    fun `should send message and return ok when type is message_new`() {
        val requestBody = """
            {
                "type": "message_new",
                "object": {
                    "message": {
                        "peer_id": 12345,
                        "text": "Hello"
                    }
                }
            }
        """.trimIndent()

        val result: MvcResult = mockMvc.perform(
            MockMvcRequestBuilders.post("/callback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andReturn()

        verify(vkMessageService).sendMessage(12345, "Hello")
        assertEquals("ok", result.response.contentAsString)
    }

    @Test
    fun `should return ok when type is unexpected`() {
        val requestBody = """
            {
                "type": "unexpected_type"
            }
        """.trimIndent()

        val result: MvcResult = mockMvc.perform(
            MockMvcRequestBuilders.post("/callback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andReturn()

        assertEquals("ok", result.response.contentAsString)
    }

    @Test
    fun `should return ok when type is missing`() {
        val requestBody = """
            {}
        """.trimIndent()

        val result: MvcResult = mockMvc.perform(
            MockMvcRequestBuilders.post("/callback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        ).andReturn()
        
        assertEquals("ok", result.response.contentAsString)
    }
}
