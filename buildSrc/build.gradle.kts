plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.10")
    implementation("org.jetbrains.kotlin:kotlin-allopen:2.0.10")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.3.4")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.6")
}