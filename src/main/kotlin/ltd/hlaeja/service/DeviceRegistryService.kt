package ltd.hlaeja.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import java.util.UUID
import ltd.hlaeja.library.deviceRegistry.Identity
import ltd.hlaeja.property.DeviceRegistryProperty
import ltd.hlaeja.util.deviceRegistryIdentityDevice
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE
import org.springframework.stereotype.Service
import org.springframework.web.ErrorResponseException
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.server.ResponseStatusException

private val log = KotlinLogging.logger {}

@Service
class DeviceRegistryService(
    meterRegistry: MeterRegistry,
    private val webClient: WebClient,
    private val deviceRegistryProperty: DeviceRegistryProperty,
) {

    private val identityDeviceSuccess = Counter.builder("device.identity.success")
        .description("Number of successful device identity calls")
        .register(meterRegistry)

    private val identityDeviceFailure = Counter.builder("device.identity.failure")
        .description("Number of failed device identity calls")
        .register(meterRegistry)

    @Cacheable(value = ["identity"], key = "#device")
    suspend fun getIdentityFromDevice(
        device: UUID,
    ): Identity.Response = try {
        webClient.deviceRegistryIdentityDevice(device, deviceRegistryProperty)
            .also { identityDeviceSuccess.increment() }
    } catch (e: ErrorResponseException) {
        identityDeviceFailure.increment()
        throw e
    } catch (e: WebClientRequestException) {
        identityDeviceFailure.increment()
        log.error(e) { "Error device identity" }
        throw ResponseStatusException(SERVICE_UNAVAILABLE)
    }
}
