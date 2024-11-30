package ltd.hlaeja

import ltd.hlaeja.property.DeviceConfigurationProperty
import ltd.hlaeja.property.DeviceDataProperty
import ltd.hlaeja.property.DeviceRegistryProperty
import ltd.hlaeja.property.JwtProperty
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(
    DeviceConfigurationProperty::class,
    DeviceDataProperty::class,
    DeviceRegistryProperty::class,
    JwtProperty::class,
)
@SpringBootApplication
class Application

fun main(vararg args: String) {
    runApplication<Application>(*args)
}
