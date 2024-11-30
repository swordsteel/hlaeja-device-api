package ltd.hlaeja.service

import java.util.UUID
import ltd.hlaeja.library.deviceRegistry.Identity
import ltd.hlaeja.property.DeviceRegistryProperty
import ltd.hlaeja.util.logCall
import org.springframework.http.HttpStatus.NOT_ACCEPTABLE
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.REQUEST_TIMEOUT
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.server.ResponseStatusException

@Service
class DeviceRegistryService(
    private val webClient: WebClient,
    private val deviceRegistryProperty: DeviceRegistryProperty,
) {

    suspend fun getIdentityFromDevice(
        device: UUID,
    ): Identity.Response = webClient.get()
        .uri("${deviceRegistryProperty.url}/identity/device-$device".also(::logCall))
        .retrieve()
        .onStatus(NOT_FOUND::equals) { throw ResponseStatusException(NOT_ACCEPTABLE) }
        .awaitBodyOrNull<Identity.Response>() ?: throw ResponseStatusException(REQUEST_TIMEOUT)
}
