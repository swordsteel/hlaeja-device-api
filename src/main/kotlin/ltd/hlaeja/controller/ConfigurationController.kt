package ltd.hlaeja.controller

import ltd.hlaeja.service.DeviceConfigurationService
import ltd.hlaeja.service.JwtService
import ltd.hlaeja.util.toDeviceResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/configuration")
class ConfigurationController(
    private val configurationService: DeviceConfigurationService,
    private val jwtService: JwtService,
) {

    @GetMapping
    suspend fun getNodeConfiguration(
        @RequestHeader("Identity") identityToken: String,
    ): Map<String, String> = jwtService.getIdentity(identityToken)
        .let { configurationService.getConfiguration(it.node).toDeviceResponse() }
}
