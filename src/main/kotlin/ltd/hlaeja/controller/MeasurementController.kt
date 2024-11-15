package ltd.hlaeja.controller

import ltd.hlaeja.library.deviceData.MeasurementData
import ltd.hlaeja.library.deviceRegistry.Identity
import ltd.hlaeja.service.DeviceDataService
import ltd.hlaeja.service.DeviceRegistryService
import ltd.hlaeja.service.JwtService
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
    private val deviceDataService: DeviceDataService,
    private val deviceRegistry: DeviceRegistryService,
    private val jwtService: JwtService,
) {

    @GetMapping
    suspend fun getNodeMeasurement(
        @RequestHeader("Identity") identityToken: String,
    ): Map<String, Number> = extracted(identityToken)
        .let { deviceDataService.getMeasurement(it.client, it.node).fields }

    @PostMapping
    @ResponseStatus(CREATED)
    suspend fun addUnitMeasurement(
        @RequestHeader("Identity") identityToken: String,
        @RequestBody measurement: Map<String, Number>,
    ) {
        return extracted(identityToken)
            .let {
                deviceDataService.addMeasurement(
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

    private suspend fun extracted(
        identityToken: String,
    ): Identity.Response = jwtService.readIdentity(identityToken)
        .let { deviceRegistry.getIdentityFromDevice(it) }
}
