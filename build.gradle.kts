plugins {
    alias(hlaeja.plugins.kotlin.jvm)
    alias(hlaeja.plugins.kotlin.spring)
    alias(hlaeja.plugins.ltd.hlaeja.plugin.service)
    alias(hlaeja.plugins.spring.dependency.management)
    alias(hlaeja.plugins.springframework.boot)
}

dependencies {
    implementation(hlaeja.com.fasterxml.jackson.module.kotlin)
    implementation(hlaeja.jjwt.api)
    implementation(hlaeja.kotlin.logging)
    implementation(hlaeja.kotlin.reflect)
    implementation(hlaeja.kotlinx.coroutines)
    implementation(hlaeja.ltd.hlaeja.library.common.messages)
    implementation(hlaeja.org.springframework.springboot.actuator.starter)
    implementation(hlaeja.org.springframework.springboot.webflux.starter)

    runtimeOnly(hlaeja.jjwt.impl)
    runtimeOnly(hlaeja.jjwt.jackson)

    testImplementation(hlaeja.io.mockk)
    testImplementation(hlaeja.io.projectreactor.reactor.test)
    testImplementation(hlaeja.kotlin.test.junit5)
    testImplementation(hlaeja.kotlinx.coroutines.test)
    testImplementation(hlaeja.org.springframework.springboot.test.starter)

    testRuntimeOnly(hlaeja.org.junit.platform.launcher)
}

group = "ltd.hlaeja"

tasks {
    named("processResources") {
        dependsOn("copyKeystore", "copyPublicKey")
    }
    register<Copy>("copyKeystore") {
        group = "hlaeja"
        from("cert/keystore.p12")
        into("${layout.buildDirectory.get()}/resources/main/cert")
        onlyIf { file("cert/keystore.p12").exists() }
    }
    register<Copy>("copyPublicKey") {
        group = "hlaeja"
        from("cert/public_key.pem")
        into("${layout.buildDirectory.get()}/resources/main/cert")
        onlyIf { file("cert/public_key.pem").exists() }
    }
}
