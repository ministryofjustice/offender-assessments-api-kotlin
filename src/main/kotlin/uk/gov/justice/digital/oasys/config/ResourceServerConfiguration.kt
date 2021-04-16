package uk.gov.justice.digital.oasys.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
class ResourceServerConfiguration : WebSecurityConfigurerAdapter() {

  @Autowired(required = false)
  private val buildProperties: BuildProperties? = null

  /**
   * @return health data. Note this is unsecured so no sensitive data allowed!
   */
  private val version: String
    get() = if (buildProperties == null) "version not available" else buildProperties.version

  @Throws(Exception::class)
  override fun configure(http: HttpSecurity) {

    http.headers().frameOptions().sameOrigin().and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

      // Can't have CSRF protection as requires session
      .and().csrf().disable()
      .authorizeRequests { auth ->
        auth.antMatchers(
          "/webjars/**", "/favicon.ico", "/csrf",
          "/health/**", "/info", "/ping",
          "/v2/api-docs",
          "/v3/api-docs",
          "/swagger-ui/**", "/swagger-resources", "/swagger-resources/configuration/ui",
          "/swagger-resources/configuration/security"
        ).permitAll()
          .antMatchers("/authentication/**").hasAnyRole("OASYS_AUTHENTICATION", "OASYS_READ_ONLY")
          .antMatchers("/**").hasAnyRole("OASYS_READ_ONLY")
          .anyRequest()
          .authenticated()
      }.oauth2ResourceServer().jwt().jwtAuthenticationConverter(AuthAwareTokenConverter())
  }
}
