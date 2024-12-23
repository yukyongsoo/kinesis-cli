import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
    id("com.vaadin") version "24.5.8"
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
}

group = "com.yuk"
version = "0.3.5-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { url = uri("https://maven.vaadin.com/vaadin-addons") }
    // for org.vaadin.olli:clipboardhelper:2.0.0
    maven { url = uri("https://maven.vaadin.com/vaadin-prereleases") }
}

extra["springCloudVersion"] = "2024.0.0"
extra["vaadinVersion"] = "24.5.8"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    implementation("com.vaadin:vaadin-spring-boot-starter")
    implementation("org.vaadin.olli:file-download-wrapper:7.1.0")
    implementation("org.vaadin.olli:clipboardhelper:2.0.0")
    implementation("com.storedobject.chart:so-charts:3.0.0")
    implementation("org.vaadin.addons.componentfactory:vcf-gridlayout:1.0.1")
    implementation("com.hilerio:ace-widget:2.0.0")
    implementation("org.vaadin:spinkit:3.0.0")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv")

    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation(platform("com.amazonaws:aws-java-sdk-bom:1.12.779"))
    implementation("com.amazonaws:aws-java-sdk-kinesis")
    implementation("com.amazonaws:amazon-kinesis-client:1.15.2")
}

dependencyManagement {
    imports {
        mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
