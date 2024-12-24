package ltd.hlaeja.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import java.util.UUID
import ltd.hlaeja.library.deviceRegistry.Identity
import ltd.hlaeja.property.JwtProperty
import ltd.hlaeja.util.PublicKeyProvider
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

private val log = KotlinLogging.logger {}

@Service
class JwtService(
    jwtProperty: JwtProperty,
    private val deviceRegistry: DeviceRegistryService,
) {

    private val parser: JwtParser = Jwts.parser()
        .verifyWith(PublicKeyProvider.load(jwtProperty.publicKey))
        .build()

    suspend fun getIdentity(
        identityToken: String,
    ): Identity.Response = try {
        readIdentity(identityToken)
            .let { deviceRegistry.getIdentityFromDevice(it) }
    } catch (e: JwtException) {
        log.warn { e.localizedMessage }
        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    private suspend fun readIdentity(
        identity: String,
    ): UUID = parser.parseSignedClaims(identity)
        .let { UUID.fromString(it.payload["device"] as String) }
        .also { log.debug { "Identified client device: $it" } }
}
