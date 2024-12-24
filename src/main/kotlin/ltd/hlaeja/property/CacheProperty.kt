package ltd.hlaeja.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cache")
data class CacheProperty(
    val timeToLive: Long,
)
