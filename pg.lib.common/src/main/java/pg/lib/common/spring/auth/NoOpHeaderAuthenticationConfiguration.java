package pg.lib.common.spring.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pg.lib.common.spring.storage.HeadersHolder;
import pg.lib.common.spring.storage.ThreadLocalHeadersHolder;

import java.util.Optional;

@Configuration
public class NoOpHeaderAuthenticationConfiguration {

    @Bean
    public HeaderAuthenticationFilter noOpHeaderAuthenticationFilter(final Optional<HeadersHolder> headersHolder) {
        return new NoOpHeaderAuthenticationFilter(headersHolder.orElse(new ThreadLocalHeadersHolder()));
    }
}
