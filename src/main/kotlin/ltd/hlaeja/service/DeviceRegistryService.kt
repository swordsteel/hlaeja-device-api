package ltd.hlaeja.service

import java.util.UUID
import ltd.hlaeja.library.deviceRegistry.Identity
import ltd.hlaeja.property.DeviceRegistryProperty
import mu.KotlinLogging
import org.springframework.http.HttpStatus.NOT_ACCEPTABLE
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.REQUEST_TIMEOUT
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.server.ResponseStatusException

private val log = KotlinLogging.logger {}

@Service
class DeviceRegistryService(
    private val webClient: WebClient,
    private val deviceRegistryProperty: DeviceRegistryProperty,
) {

    suspend fun getIdentityFromDevice(
        device: UUID,
    ): Identity.Response = webClient.get()
        .uri("${deviceRegistryProperty.url}/identity/device-$device")
        .also { log.debug("{}/node/device-{}", deviceRegistryProperty.url, device) }
        .retrieve()
        .onStatus(NOT_FOUND::equals) { throw ResponseStatusException(NOT_ACCEPTABLE) }
        .awaitBodyOrNull<Identity.Response>() ?: throw ResponseStatusException(REQUEST_TIMEOUT)
}
