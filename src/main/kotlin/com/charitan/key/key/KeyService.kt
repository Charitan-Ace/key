package com.charitan.key.key

import com.charitan.key.jwt.JwtInternalService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Curve
import io.jsonwebtoken.security.Jwks
import io.jsonwebtoken.security.SignatureAlgorithm
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
internal class KeyService(
    private val jwtService: JwtInternalService,
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val objectMapper: ObjectMapper,
) : KeyInternalService {
    private final val encAlgorithm: SignatureAlgorithm = Jwts.SIG.RS256
    private final val sigCurve: Curve = Jwks.CRV.Ed25519

    private final val serializer = JacksonSerializer<Any>()

    @Bean
    fun keyTopics() =
        KafkaAdmin.NewTopics(
            *KeyTopic.entries
                .map {
                    TopicBuilder
                        .name(it.topic)
                        .partitions(1)
                        .replicas(2)
                        .build()
                }.toTypedArray(),
        )

    override fun updateEncryptionKey() {
        val encPair = encAlgorithm.keyPair().build()
        val jwk = jwtService.generateRsaJwk(encPair)

        kafkaTemplate.send(
            "key.encryption.private.change",
            objectMapper.readValue<Map<String, String>>(String(serializer.serialize(jwk))),
        )
        kafkaTemplate.send(
            "key.encryption.public.change",
            objectMapper.readValue<Map<String, String>>(String(serializer.serialize(jwk.toPublicJwk()))),
        )
    }

    override fun updateSignatureKey() {
        val sigPair = sigCurve.keyPair().build()
        val jwk = jwtService.generateEcJwk(sigPair)

        kafkaTemplate.send(
            "key.signature.private.change",
            objectMapper.readValue<Map<String, String>>(String(serializer.serialize(jwk))),
        )
        kafkaTemplate.send(
            "key.signature.public.change",
            objectMapper.readValue<Map<String, String>>(String(serializer.serialize(jwk.toPublicJwk()))),
        )
    }
}
