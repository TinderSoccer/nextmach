plugins {
    id("org.springframework.boot") version "3.2.4"
    java
}

java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
    val bootVersion = "3.2.4"
    implementation("org.springframework.boot:spring-boot-starter-web:$bootVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$bootVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:$bootVersion")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$bootVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
