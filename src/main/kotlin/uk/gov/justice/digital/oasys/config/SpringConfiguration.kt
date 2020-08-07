package uk.gov.justice.digital.oasys.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import uk.gov.justice.digital.oasys.utils.RequestData

@Configuration
class SpringConfiguration : WebMvcConfigurer {

    @Value("\${logging.uris.exclude.regex}")
    private val excludedLogUrls : String? = null

    @Bean(name = ["globalObjectMapper"])
    @Primary
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
                .registerModules(Jdk8Module(), JavaTimeModule())
    }

    @Bean
    fun mappingJackson2HttpMessageConverter(@Qualifier("globalObjectMapper") objectMapper: ObjectMapper?): MappingJackson2HttpMessageConverter {
        val jsonConverter = MappingJackson2HttpMessageConverter()
        jsonConverter.objectMapper = objectMapper
        return jsonConverter
    }

    @Bean
    fun createRequestData(): RequestData {
        return RequestData(excludedLogUrls)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(createRequestData())
    }
}