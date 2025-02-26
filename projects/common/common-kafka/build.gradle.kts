plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("io.spring.dependency-management") version "1.1.6"
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.2.10")
    }
}

version = "1.0.0-SNAPSHOT"

dependencies {
    implementation("org.apache.kafka", "kafka-clients")

    implementation("io.cloudevents", "cloudevents-json-jackson")
    implementation("io.cloudevents", "cloudevents-spring")
    implementation("io.cloudevents", "cloudevents-kafka") {
        exclude("org.apache.kafka", "kafka-clients")
    }

    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.slf4j:slf4j-api")
    implementation("ch.qos.logback:logback-classic")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("net.logstash.logback", "logstash-logback-encoder")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
