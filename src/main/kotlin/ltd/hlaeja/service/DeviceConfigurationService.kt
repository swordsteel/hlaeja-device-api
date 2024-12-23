package ltd.hlaeja.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import java.util.UUID
import ltd.hlaeja.library.deviceConfiguration.Node
import ltd.hlaeja.property.DeviceConfigurationProperty
import ltd.hlaeja.util.deviceConfigurationGetConfiguration
import org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE
import org.springframework.stereotype.Service
import org.springframework.web.ErrorResponseException
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.server.ResponseStatusException

private val log = KotlinLogging.logger {}

@Service
class DeviceConfigurationService(
    meterRegistry: MeterRegistry,
    private val webClient: WebClient,
    private val deviceConfigurationProperty: DeviceConfigurationProperty,
) {

    private val deviceConfigurationSuccess = Counter.builder("device.configuration.success")
        .description("Number of successful device configuration calls")
        .register(meterRegistry)

    private val deviceConfigurationFailure = Counter.builder("device.configuration.failure")
        .description("Number of failed device configuration calls")
        .register(meterRegistry)

    suspend fun getConfiguration(
        node: UUID,
    ): Node.Response = try {
        webClient.deviceConfigurationGetConfiguration(node, deviceConfigurationProperty)
            .also { deviceConfigurationSuccess.increment() }
    } catch (e: ErrorResponseException) {
        deviceConfigurationFailure.increment()
        throw e
    } catch (e: WebClientRequestException) {
        deviceConfigurationFailure.increment()
        log.error(e) { "Error device registry" }
        throw ResponseStatusException(SERVICE_UNAVAILABLE)
    }
}
