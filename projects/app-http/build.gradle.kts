plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.2.10"
    id("io.spring.dependency-management") version "1.1.6"
}

version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(
        project(":sample.redis")
    )

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("net.logstash.logback", "logstash-logback-encoder")

    implementation(platform("io.micrometer:micrometer-tracing-bom:1.4.1"))
    implementation("io.micrometer", "micrometer-tracing-bridge-brave") {
        exclude("aopalliance")
    }

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
