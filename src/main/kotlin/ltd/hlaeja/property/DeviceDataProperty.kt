package ltd.hlaeja.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "device-data")
data class DeviceDataProperty(
    val url: String,
)
