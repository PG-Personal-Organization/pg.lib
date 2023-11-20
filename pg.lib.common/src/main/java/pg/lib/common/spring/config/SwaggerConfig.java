package pg.lib.common.spring.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Swagger configuration.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Public api grouped open api.
     *
     * @return the grouped open api
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/**")
                .pathsToExclude("/")
                .build();
    }

    /**
     * V 1 api grouped open api.
     *
     * @return the grouped open api
     */
    @Bean
    public GroupedOpenApi frontendApi() {
        return GroupedOpenApi.builder()
                .group("frontend")
                .pathsToMatch("/api/frontend/**")
                .pathsToExclude("/")
                .build();
    }

    /**
     * V 1 api grouped open api.
     *
     * @return the grouped open api
     */
    @Bean
    public GroupedOpenApi cqrsApi() {
        return GroupedOpenApi.builder()
                .group("cqrs")
                .pathsToMatch("/api/cqrs/**")
                .pathsToExclude("/")
                .build();
    }

    /**
     * Lab 2 open api.
     *
     * @return the open api
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("API v1")
                        .description("Simple API")
                        .version("v0.2")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Simple API Wiki Documentation")
                        .url("https://github.com/PG-Personal-Organization"));
    }
}
