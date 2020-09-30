
plugins {
    id("uk.gov.justice.hmpps.gradle-spring-boot") version "1.0.5"
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"
    kotlin("plugin.allopen") version "1.3.61"
    kotlin("kapt") version "1.3.72"
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

configurations {
    implementation { exclude(mapOf("module" to "tomcat-jdbc")) }
}

dependencyCheck {
    suppressionFiles.add("hmpps-assessments-api-suppressions.xml")
}

dependencies {

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("com.querydsl:querydsl-apt:4.3.1")
    implementation("com.querydsl:querydsl-jpa:4.3.1")
    kapt("com.querydsl:querydsl-apt:4.3.1:jpa")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
    implementation("javax.activation:activation:1.1.1")
    implementation("com.sun.xml.bind:jaxb-impl:3.0.0-M1")
    implementation("com.sun.xml.bind:jaxb-core:2.3.0.1")
    implementation("javax.inject:javax.inject:1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.11.0.rc1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.11.0.rc1")
    implementation("io.springfox:springfox-swagger2:2.9.2")
    implementation("io.springfox:springfox-swagger-ui:2.9.2")
    implementation("commons-io:commons-io:2.6")
    implementation("com.zaxxer:HikariCP:3.4.2")

    implementation( "com.google.code.gson:gson:2.8.6")
    implementation("com.google.guava:guava:29.0-jre")

    implementation(files("lib/ojdbc8-12.2.0.1.jar"))
    implementation("org.apache.commons:commons-lang3:3.10")
    runtimeOnly("com.h2database:h2:1.4.200")
    runtimeOnly("org.flywaydb:flyway-core:6.5.6")

    testRuntimeOnly("com.h2database:h2:1.4.200")
    testAnnotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation("com.ninja-squad:springmockk:2.0.1")
    testImplementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("com.nimbusds:nimbus-jose-jwt:8.17")
    testImplementation("com.github.tomakehurst:wiremock-standalone:2.26.3")
}

