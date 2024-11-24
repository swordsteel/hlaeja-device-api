package ltd.hlaeja.service

import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import java.util.UUID
import ltd.hlaeja.property.JwtProperty
import ltd.hlaeja.util.PublicKeyProvider
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
class JwtService(
    jwtProperty: JwtProperty,
) {

    private val parser: JwtParser = Jwts.parser()
        .verifyWith(PublicKeyProvider.load(jwtProperty.publicKey))
        .build()

    suspend fun readIdentity(
        identity: String,
    ): UUID = parser.parseSignedClaims(identity)
        .let { UUID.fromString(it.payload["device"] as String) }
        .also { log.debug("Identified client device: {}", it) }
}
