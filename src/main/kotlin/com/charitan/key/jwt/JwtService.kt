package com.charitan.key.jwt

import io.jsonwebtoken.security.Jwks
import io.jsonwebtoken.security.OctetPrivateJwk
import io.jsonwebtoken.security.RsaPrivateJwk
import org.bouncycastle.jcajce.interfaces.EdDSAPrivateKey
import org.bouncycastle.jcajce.interfaces.EdDSAPublicKey
import org.springframework.stereotype.Service
import java.security.KeyPair

@Service
internal class JwtService : JwtInternalService {
    override fun generateRsaJwk(keyPair: KeyPair): RsaPrivateJwk =
        Jwks
            .builder()
            .rsaKeyPair(keyPair)
            .idFromThumbprint()
            .build()

    override fun generateEcJwk(keyPair: KeyPair): OctetPrivateJwk<EdDSAPrivateKey, EdDSAPublicKey> =
        Jwks
            .builder()
            .octetKeyPair<EdDSAPrivateKey, EdDSAPublicKey>(keyPair)
            .idFromThumbprint()
            .build()
}
