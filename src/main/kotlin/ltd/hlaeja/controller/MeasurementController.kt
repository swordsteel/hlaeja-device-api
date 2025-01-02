package ltd.hlaeja.controller

import java.util.UUID
import ltd.hlaeja.jwt.service.PublicJwtService
import ltd.hlaeja.library.deviceData.MeasurementData
import ltd.hlaeja.library.deviceRegistry.Identity
import ltd.hlaeja.service.DeviceDataService
import ltd.hlaeja.service.DeviceRegistryService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/measurement")
class MeasurementController(
    private val dataService: DeviceDataService,
    private val deviceRegistry: DeviceRegistryService,
    private val publicJwtService: PublicJwtService,
) {

    @GetMapping
    suspend fun getNodeMeasurement(
        @RequestHeader("Identity") identityToken: String,
    ): Map<String, Number> = readIdentityToken(identityToken)
        .let { dataService.getMeasurement(it.client, it.node).fields }

    @PostMapping
    @ResponseStatus(CREATED)
    suspend fun addUnitMeasurement(
        @RequestHeader("Identity") identityToken: String,
        @RequestBody measurement: Map<String, Number>,
    ) {
        return readIdentityToken(identityToken)
            .let {
                dataService.addMeasurement(
                    it.client,
                    MeasurementData.Request(
                        mutableMapOf(
                            "node" to it.node.toString(),
                            "device" to it.device.toString(),
                        ),
                        measurement,
                    ),
                )
            }
    }

    private suspend fun readIdentityToken(identityToken: String): Identity.Response = publicJwtService
        .verify(identityToken) { claims -> UUID.fromString(claims.payload["device"] as String) }
        .let { deviceRegistry.getIdentityFromDevice(it) }
}
