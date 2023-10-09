import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktor_swagger_version = "2.5.0"

plugins {
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.4"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

group = "ru.students"
version = "0.0.1"

application {
    mainClass.set("ru.students.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-swagger-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-jackson-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.ktorm:ktorm-core:3.6.0")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("org.flywaydb:flyway-core:9.22.0")
    implementation("io.github.smiley4:ktor-swagger-ui:$ktor_swagger_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}