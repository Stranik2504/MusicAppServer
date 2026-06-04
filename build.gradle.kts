plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    application
}

group = "dev.stranik"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.defaultHeaders)
    implementation(ktorLibs.server.netty)
    implementation("ch.qos.logback:logback-classic:1.5.25")
    implementation("ch.qos.logback:logback-core:1.5.25")

    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-status-pages-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")

    // Auth
    implementation("io.ktor:ktor-server-auth-jwt:3.5.0")
    implementation("com.auth0:java-jwt:4.4.0")

    // Password hash
    implementation("at.favre.lib:bcrypt:0.10.2")

    // Exposed + PostgreSQL
    implementation("org.jetbrains.exposed:exposed-core:0.55.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.55.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.55.0")
    implementation("org.postgresql:postgresql:42.7.11")

    // For add date
    implementation("org.jetbrains.exposed:exposed-java-time:0.55.0")

    // Pull connections
    implementation("com.zaxxer:HikariCP:6.0.0")

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
