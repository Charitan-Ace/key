package com.charitan.key.key

enum class KeyTopic(
    val topic: String,
) {
    ENCRYPTION_PRIVATE_CHANGE("key.encryption.private.change"),
    ENCRYPTION_PUBLIC_CHANGE("key.encryption.public.change"),
    SIGNATURE_PRIVATE_CHANGE("key.signature.private.change"),
    SIGNATURE_PUBLIC_CHANGE("key.signature.public.change"),
}
