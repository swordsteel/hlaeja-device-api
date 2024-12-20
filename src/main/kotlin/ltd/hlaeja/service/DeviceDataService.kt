package ltd.hlaeja.service

import java.util.UUID
import ltd.hlaeja.library.deviceData.MeasurementData
import ltd.hlaeja.property.DeviceDataProperty
import ltd.hlaeja.util.logCall
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.REQUEST_TIMEOUT
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.server.ResponseStatusException

@Service
class DeviceDataService(
    private val webClient: WebClient,
    private val deviceDataProperty: DeviceDataProperty,
) {

    suspend fun getMeasurement(
        client: UUID,
        node: UUID,
    ): MeasurementData.Response = webClient.get()
        .uri("${deviceDataProperty.url}/client-$client/node-$node".also(::logCall))
        .retrieve()
        .onStatus(NOT_FOUND::equals) { throw ResponseStatusException(NO_CONTENT) }
        .awaitBodyOrNull<MeasurementData.Response>() ?: throw ResponseStatusException(REQUEST_TIMEOUT)

    suspend fun addMeasurement(
        client: UUID,
        request: MeasurementData.Request,
    ) = webClient.post()
        .uri("${deviceDataProperty.url}/client-$client".also(::logCall))
        .bodyValue(request)
        .retrieve()
        .awaitBodilessEntity()
}
