package pg.lib.common.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

/**
 * The type Common module configuration.
 */
@Configuration
@Import({
        CommonSecurityConfig.class,
        CommonSwaggerConfig.class,
        CommonTracingConfig.class
})
public class CommonModuleConfiguration {

    @Bean
    @Primary
    public ObjectMapper basicObjectMapper() {
        return new ObjectMapper();
    }

}