plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.cosmos"
version = "1.0.0-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

subprojects {

	apply(plugin = "kotlin")
	apply(plugin = "io.spring.dependency-management")

	repositories {
		mavenCentral()
	}

	dependencyManagement {
		dependencies {
			dependency("net.logstash.logback:logstash-logback-encoder:6.6")

			dependency("org.apache.kafka:kafka-clients:3.4.1")
			dependency("io.cloudevents:cloudevents-json-jackson:2.4.1")
			dependency("io.cloudevents:cloudevents-spring:2.3.0")
			dependency("io.cloudevents:cloudevents-kafka:2.4.1")
		}
	}

	kotlin {
		compilerOptions {
			freeCompilerArgs.addAll("-Xjsr305=strict")
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}
