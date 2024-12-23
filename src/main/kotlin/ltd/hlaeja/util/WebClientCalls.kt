package ltd.hlaeja.util

import java.util.UUID
import ltd.hlaeja.library.deviceConfiguration.Node
import ltd.hlaeja.library.deviceData.MeasurementData
import ltd.hlaeja.library.deviceRegistry.Identity
import ltd.hlaeja.property.DeviceConfigurationProperty
import ltd.hlaeja.property.DeviceDataProperty
import ltd.hlaeja.property.DeviceRegistryProperty
import org.springframework.http.HttpStatus.NOT_ACCEPTABLE
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.REQUEST_TIMEOUT
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.server.ResponseStatusException

suspend fun WebClient.deviceRegistryIdentityDevice(
    device: UUID,
    property: DeviceRegistryProperty,
): Identity.Response = get()
    .uri("${property.url}/identity/device-$device".also(::logCall))
    .retrieve()
    .onStatus(NOT_FOUND::equals) { throw ResponseStatusException(NOT_ACCEPTABLE) }
    .awaitBodyOrNull<Identity.Response>() ?: throw ResponseStatusException(REQUEST_TIMEOUT)

suspend fun WebClient.deviceDataGetMeasurement(
    client: UUID,
    node: UUID,
    property: DeviceDataProperty,
): MeasurementData.Response = get()
    .uri("${property.url}/client-$client/node-$node".also(::logCall))
    .retrieve()
    .onStatus(NOT_FOUND::equals) { throw ResponseStatusException(NO_CONTENT) }
    .awaitBodyOrNull<MeasurementData.Response>() ?: throw ResponseStatusException(REQUEST_TIMEOUT)

suspend fun WebClient.deviceDataAddMeasurement(
    client: UUID,
    request: MeasurementData.Request,
    property: DeviceDataProperty,
): ResponseEntity<Void> = post()
    .uri("${property.url}/client-$client".also(::logCall))
    .bodyValue(request)
    .retrieve()
    .awaitBodilessEntity()

suspend fun WebClient.deviceConfigurationGetConfiguration(
    node: UUID,
    property: DeviceConfigurationProperty,
): Node.Response = get()
    .uri("${property.url}/node-$node".also(::logCall))
    .retrieve()
    .onStatus(NOT_FOUND::equals) { throw ResponseStatusException(NO_CONTENT) }
    .awaitBodyOrNull<Node.Response>() ?: throw ResponseStatusException(REQUEST_TIMEOUT)
