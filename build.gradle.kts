import com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer

plugins {
    alias(hlaeja.plugins.kotlin.jvm)
    alias(hlaeja.plugins.kotlin.spring)
    alias(hlaeja.plugins.ltd.hlaeja.plugin.certificate)
    alias(hlaeja.plugins.ltd.hlaeja.plugin.service)
    alias(hlaeja.plugins.spring.dependency.management)
    alias(hlaeja.plugins.springframework.boot)
}

dependencies {
    implementation(hlaeja.fasterxml.jackson.module.kotlin)
    implementation(hlaeja.jjwt.api)
    implementation(hlaeja.kotlin.logging)
    implementation(hlaeja.kotlin.reflect)
    implementation(hlaeja.kotlinx.coroutines)
    implementation(hlaeja.micrometer.registry.influx)
    implementation(hlaeja.library.hlaeja.common.messages)
    implementation(hlaeja.springboot.starter.actuator)
    implementation(hlaeja.springboot.starter.cache)
    implementation(hlaeja.springboot.starter.redis)
    implementation(hlaeja.springboot.starter.webflux)

    runtimeOnly(hlaeja.jjwt.impl)
    runtimeOnly(hlaeja.jjwt.jackson)

    testImplementation(hlaeja.kotlin.test.junit5)
    testImplementation(hlaeja.kotlinx.coroutines.test)
    testImplementation(hlaeja.mockk)
    testImplementation(hlaeja.projectreactor.reactor.test)
    testImplementation(hlaeja.springboot.starter.test)

    testRuntimeOnly(hlaeja.junit.platform.launcher)
}

group = "ltd.hlaeja"

fun influxDbToken(): String = config.findOrDefault("influxdb.token", "INFLUXDB_TOKEN", "")

tasks {
    named("containerCreate", DockerCreateContainer::class) {
        withEnvVar("MANAGEMENT_INFLUX_METRICS_EXPORT_TOKEN", influxDbToken())
    }
    withType<ProcessResources> {
        filesMatching("**/application.yml") { filter { it.replace("%INFLUXDB_TOKEN%", influxDbToken()) } }
        onlyIf { file("src/main/resources/application.yml").exists() }
    }
    named("processResources") {
        dependsOn("copyCertificates")
    }
}
