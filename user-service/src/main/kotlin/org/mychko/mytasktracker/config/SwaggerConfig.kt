package org.mychko.mytasktracker.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Configuration
    class SwaggerConfig {

        @Bean
        fun customOpenAPI(): OpenAPI {
            return OpenAPI()
                .info(
                    Info()
                        .title("My Task Tracker API")
                        .version("1.0.0")
                        .description("REST API для трекера задач")
                )
        }
    }
}