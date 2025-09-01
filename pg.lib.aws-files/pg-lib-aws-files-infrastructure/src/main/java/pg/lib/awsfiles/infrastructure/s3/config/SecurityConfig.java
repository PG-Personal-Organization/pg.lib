package pg.lib.awsfiles.infrastructure.s3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import pg.lib.awsfiles.infrastructure.common.HttpServicesPaths;
import pg.lib.common.spring.user.Roles;

@Configuration
public class SecurityConfig {

    @Bean
    public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> filesRequestCustomizer() {
        return requests -> requests.requestMatchers("/" + HttpServicesPaths.BASE_PATH + "/**")
                .hasRole(Roles.USER.name());
    }
}
