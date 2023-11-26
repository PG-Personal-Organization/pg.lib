package pg.lib.common.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The type Common module configuration.
 */
@Configuration
@Import({
        CommonSecurityConfig.class,
        CommonSwaggerConfig.class
})
public class CommonModuleConfiguration {

}