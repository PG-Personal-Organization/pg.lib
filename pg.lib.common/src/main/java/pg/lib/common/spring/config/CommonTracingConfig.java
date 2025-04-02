package pg.lib.common.spring.config;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pg.lib.common.spring.storage.HeadersHolder;
import pg.lib.common.spring.tracing.LoggingFilter;

@Configuration
public class CommonTracingConfig {
    @Bean
    public LoggingFilter loggingFilter(final @NonNull HeadersHolder headersHolder) {
        return new LoggingFilter(headersHolder);
    }
}
