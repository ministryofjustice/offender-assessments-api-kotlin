package uk.gov.justice.digital.oasys.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.Date

@Configuration
class SwaggerConfiguration {
  @Autowired(required = false)
  private val buildProperties: BuildProperties? = null

  @Bean
  fun api(): Docket {
    val apiInfo = ApiInfo(
      "HMPPS Offender Assessment API",
      "OASys Data API.",
      version,
      "",
      contactInfo(),
      "Open Government Licence v3.0", "https://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/",
      emptyList()
    )
    val docket = Docket(DocumentationType.OAS_30)
      .useDefaultResponseMessages(false)
      .apiInfo(apiInfo)
      .select()
      .apis(RequestHandlerSelectors.basePackage("uk.gov.justice.digital.oasys"))
      .paths(PathSelectors.any())
      .build()
    docket.directModelSubstitute(ZonedDateTime::class.java, Date::class.java)
    docket.directModelSubstitute(LocalDateTime::class.java, Date::class.java)
    return docket
  }

  /**
   * @return health data. Note this is unsecured so no sensitive data allowed!
   */
  private val version: String
    get() = if (buildProperties == null) "version not available" else buildProperties.version

  private fun contactInfo(): Contact {
    return Contact(
      "HMPPS Digital Studio",
      "",
      "feedback@digital.justice.gov.uk"
    )
  }
}
