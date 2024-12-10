package ltd.hlaeja.util

import ltd.hlaeja.library.deviceConfiguration.Node

fun Node.Response.toDeviceResponse(): Map<String, String> {
    return mapOf(
        "version" to timestamp.toEpochSecond().toString(),
        "data" to configuration,
    )
}
