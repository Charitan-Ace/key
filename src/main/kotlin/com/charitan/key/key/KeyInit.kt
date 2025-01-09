package com.charitan.key.key

import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
internal class KeyInit(
    private val keyService: KeyService,
) {
    @EventListener(ContextRefreshedEvent::class)
    fun contextRefreshedEvent() {
        keyService.updateSignatureKey()
        keyService.updateEncryptionKey()
    }
}
