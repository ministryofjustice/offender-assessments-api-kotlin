
plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "3.3.0"
  kotlin("plugin.spring") version "1.5.20"
  kotlin("plugin.jpa") version "1.5.20"
  kotlin("plugin.allopen") version "1.5.20"
  kotlin("kapt") version "1.5.20"
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
  implementation("com.querydsl:querydsl-apt:4.4.0")
  implementation("com.querydsl:querydsl-jpa:4.4.0")
  kapt("com.querydsl:querydsl-apt:4.4.0:jpa")

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
  implementation("com.sun.xml.bind:jaxb-impl:3.0.0")
  implementation("com.sun.xml.bind:jaxb-core:3.0.0")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.3")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.3")
  implementation("commons-io:commons-io:2.8.0")
  implementation("com.zaxxer:HikariCP:4.0.3")
  implementation("io.springfox:springfox-boot-starter:3.0.0")

  runtimeOnly("com.oracle.database.jdbc:ojdbc10:19.10.0.0")
  implementation("org.apache.commons:commons-lang3:3.12.0")
  runtimeOnly("com.h2database:h2:1.4.200")
  runtimeOnly("org.flywaydb:flyway-core:7.8.1")

  testRuntimeOnly("com.h2database:h2:1.4.200")
  testAnnotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    exclude(module = "mockito-core")
  }
  testImplementation("com.ninja-squad:springmockk:3.0.1")
  testImplementation("io.jsonwebtoken:jjwt:0.9.1")
  testImplementation("io.swagger.parser.v3:swagger-parser-v2-converter:2.0.21")
}
