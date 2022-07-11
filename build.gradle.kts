import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("com.vaadin") version "23.1.2"
    kotlin("jvm") version "1.6.20"
    kotlin("plugin.spring") version "1.6.20"
}

group = "com.yuk"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    maven { url = uri("https://maven.vaadin.com/vaadin-addons") }
}

extra["springCloudVersion"] = "2021.0.2"
extra["vaadinVersion"] = "23.1.2"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    implementation("com.vaadin:vaadin-spring-boot-starter")
    implementation("org.vaadin.olli:file-download-wrapper:4.0.0")
    implementation("com.storedobject.chart:so-charts:3.0.0")
    implementation("org.vaadin.addons.componentfactory:vcf-gridlayout:1.0.1")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv")

    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.shell:spring-shell-starter:2.0.1.RELEASE")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation(platform("com.amazonaws:aws-java-sdk-bom:1.12.213"))
    implementation("com.amazonaws:aws-java-sdk-kinesis")
    implementation("com.amazonaws:amazon-kinesis-client:1.14.8")
}

dependencyManagement {
    imports {
        mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
