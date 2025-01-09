package com.charitan.key.jwt

import io.jsonwebtoken.security.OctetPrivateJwk
import io.jsonwebtoken.security.RsaPrivateJwk
import org.bouncycastle.jcajce.interfaces.EdDSAPrivateKey
import org.bouncycastle.jcajce.interfaces.EdDSAPublicKey
import java.security.KeyPair

interface JwtInternalService {
    fun generateRsaJwk(keyPair: KeyPair): RsaPrivateJwk

    fun generateEcJwk(keyPair: KeyPair): OctetPrivateJwk<EdDSAPrivateKey, EdDSAPublicKey>
}
