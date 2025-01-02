package ltd.hlaeja.controller

import java.util.UUID
import ltd.hlaeja.jwt.service.PublicJwtService
import ltd.hlaeja.service.DeviceConfigurationService
import ltd.hlaeja.service.DeviceRegistryService
import ltd.hlaeja.util.toDeviceResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/configuration")
class ConfigurationController(
    private val configurationService: DeviceConfigurationService,
    private val deviceRegistry: DeviceRegistryService,
    private val publicJwtService: PublicJwtService,
) {

    @GetMapping
    suspend fun getNodeConfiguration(
        @RequestHeader("Identity") identityToken: String,
    ): Map<String, String> = readIdentityToken(identityToken)
        .let { deviceRegistry.getIdentityFromDevice(it) }
        .let { configurationService.getConfiguration(it.node).toDeviceResponse() }

    private fun readIdentityToken(identityToken: String): UUID = publicJwtService
        .verify(identityToken) { claims -> UUID.fromString(claims.payload["device"] as String) }
}
