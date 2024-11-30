package ltd.hlaeja.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "device-configuration")
data class DeviceConfigurationProperty(
    val url: String,
)
