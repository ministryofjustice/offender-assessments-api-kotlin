package uk.gov.justice.digital.oasys.config

import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.AuthorizationCodeGrantBuilder
import springfox.documentation.builders.OAuthBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.Contact
import springfox.documentation.service.SecurityReference
import springfox.documentation.service.SecurityScheme
import springfox.documentation.service.StringVendorExtension
import springfox.documentation.service.TokenEndpoint
import springfox.documentation.service.TokenRequestEndpoint
import springfox.documentation.service.VendorExtension
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.Date
import java.util.Optional
import kotlin.collections.ArrayList

@Configuration
@EnableSwagger2
class SwaggerConfiguration(buildProperties: BuildProperties) {
  private val version: String = buildProperties.version

  @Bean
  fun api(): Docket {
    val docket = Docket(DocumentationType.SWAGGER_2)
      .select()
      .apis(RequestHandlerSelectors.basePackage("uk.gov.justice.digital.oasys.controllers"))
      .paths(PathSelectors.any())
      .build()
      .securitySchemes(listOf(securityScheme()))
      .securityContexts(listOf(securityContext()))
      .apiInfo(apiInfo())
    docket.genericModelSubstitutes(Optional::class.java)
    docket.directModelSubstitute(ZonedDateTime::class.java, Date::class.java)
    docket.directModelSubstitute(LocalDateTime::class.java, Date::class.java)
    return docket
  }

  private fun securityScheme(): SecurityScheme {
    val grantType = AuthorizationCodeGrantBuilder()
      .tokenEndpoint(TokenEndpoint("http://localhost:9090/auth/oauth" + "/token", "oauthtoken"))
      .tokenRequestEndpoint(
        TokenRequestEndpoint("http://localhost:9090/auth/oauth" + "/authorize", "swagger-client", "clientsecret")
      )
      .build()
    return OAuthBuilder().name("spring_oauth")
      .grantTypes(listOf(grantType))
      .scopes(listOf(*scopes()))
      .build()
  }

  private fun scopes() = arrayOf(
    AuthorizationScope("read", "for read operations"),
    AuthorizationScope("write", "for write operations")
  )

  private fun securityContext() = SecurityContext.builder()
    .securityReferences(listOf(SecurityReference("spring_oauth", scopes())))
    .forPaths(PathSelectors.regex("/.*"))
    .build()

  private fun contactInfo() = Contact(
    "HMPPS Digital Studio",
    "",
    "feedback@digital.justice.gov.uk"
  )

  private fun apiInfo(): ApiInfo {
    val vendorExtension = StringVendorExtension("", "")
    val vendorExtensions: MutableCollection<VendorExtension<*>> = ArrayList()
    vendorExtensions.add(vendorExtension)
    return ApiInfo(
      "HMPPS Offender Assessment API",
      "OASys Data API.",
      version,
      "https://gateway.nomis-api.service.justice.gov.uk/auth/terms",
      contactInfo(),
      "MIT",
      "https://opensource.org/licenses/MIT",
      vendorExtensions
    )
  }
}
