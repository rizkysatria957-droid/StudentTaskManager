plugins {
    kotlin("jvm") version "2.2.20"
    application
}

group = "com.studentmanager"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.2")
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.studentmanager.MainKt")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
