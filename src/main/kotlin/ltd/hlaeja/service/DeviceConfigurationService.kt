package ltd.hlaeja.service

import java.util.UUID
import ltd.hlaeja.library.deviceConfiguration.Node
import ltd.hlaeja.property.DeviceConfigurationProperty
import ltd.hlaeja.util.logCall
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.REQUEST_TIMEOUT
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.server.ResponseStatusException

@Service
class DeviceConfigurationService(
    private val webClient: WebClient,
    private val deviceConfigurationProperty: DeviceConfigurationProperty,
) {

    suspend fun getConfiguration(
        node: UUID,
    ): Node.Response = webClient.get()
        .uri("${deviceConfigurationProperty.url}/node-$node".also(::logCall))
        .retrieve()
        .onStatus(NOT_FOUND::equals) { throw ResponseStatusException(NO_CONTENT) }
        .awaitBodyOrNull<Node.Response>() ?: throw ResponseStatusException(REQUEST_TIMEOUT)
}
