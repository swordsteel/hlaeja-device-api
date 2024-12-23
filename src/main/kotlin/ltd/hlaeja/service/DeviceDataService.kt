package ltd.hlaeja.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import java.util.UUID
import ltd.hlaeja.library.deviceData.MeasurementData
import ltd.hlaeja.property.DeviceDataProperty
import ltd.hlaeja.util.deviceDataAddMeasurement
import ltd.hlaeja.util.deviceDataGetMeasurement
import org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.ErrorResponseException
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.server.ResponseStatusException

private val log = KotlinLogging.logger {}

@Service
class DeviceDataService(
    meterRegistry: MeterRegistry,
    private val webClient: WebClient,
    private val deviceDataProperty: DeviceDataProperty,
) {

    private val deviceDataSuccess = Counter.builder("device.data.success")
        .description("Number of successful device data calls")
        .register(meterRegistry)

    private val deviceDataFailure = Counter.builder("device.data.failure")
        .description("Number of failed device data calls")
        .register(meterRegistry)

    suspend fun getMeasurement(
        client: UUID,
        node: UUID,
    ): MeasurementData.Response = try {
        webClient.deviceDataGetMeasurement(client, node, deviceDataProperty)
            .also { deviceDataSuccess.increment() }
    } catch (e: ErrorResponseException) {
        deviceDataFailure.increment()
        throw e
    } catch (e: WebClientRequestException) {
        deviceDataFailure.increment()
        log.error(e) { "Error device registry" }
        throw ResponseStatusException(SERVICE_UNAVAILABLE)
    }

    suspend fun addMeasurement(
        client: UUID,
        request: MeasurementData.Request,
    ): ResponseEntity<Void> = try {
        webClient.deviceDataAddMeasurement(client, request, deviceDataProperty)
            .also { deviceDataSuccess.increment() }
    } catch (e: ErrorResponseException) {
        deviceDataFailure.increment()
        throw e
    } catch (e: WebClientRequestException) {
        deviceDataFailure.increment()
        log.error(e) { "Error device registry" }
        throw ResponseStatusException(SERVICE_UNAVAILABLE)
    }
}
