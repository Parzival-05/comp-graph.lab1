import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(project(":lib.partial_system"))
    implementation(compose.desktop.currentOs)
    implementation("org.tinspin:tinspin-indexes:2.1.3")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.11.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.3")
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint") // Version should be inherited from parent
    repositories {
        mavenCentral()
    }
}

tasks.test {
    useJUnitPlatform()
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "lab-1"
            packageVersion = "1.0.0"
        }
    }
}
