package com.bot.vkbot.service

import org.springframework.stereotype.Service

@Service
class SystemTimeService {
    fun currentTimeMillis(): Long = System.currentTimeMillis()
}
