package pg.lib.remote.cqrs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pg.lib.common.spring.auth.HeaderAuthenticationFilter;
import pg.lib.common.spring.storage.HeadersHolder;
import pg.lib.remote.cqrs.executors.RemoteCqrsModuleServiceExecutor;
import pg.lib.remote.cqrs.http.HttpConfig;
import pg.lib.remote.cqrs.http.HttpModuleServiceExecutor;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * The type Remote modules cqrs configuration.
 */
@Configuration
@Import({HttpConfig.class})
public class RemoteModulesCqrsConfiguration {
    /**
     * Http client.
     *
     * @param httpConfig the http config
     * @return the http client
     */
    @Bean
    public HttpClient httpClient(final HttpConfig httpConfig) {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofMillis(httpConfig.getTimeoutInMillis()))
                .build();
    }

    /**
     * Remote service executor remote cqrs module service executor.
     *
     * @param httpConfig           the http config
     * @param objectMapper         the object mapper
     * @param authenticationFilter the authentication filter
     * @return the remote cqrs module service executor
     */
    @Bean
    public RemoteCqrsModuleServiceExecutor remoteServiceExecutor(final HttpConfig httpConfig,
                                                                 final ObjectMapper objectMapper,
                                                                 final HeaderAuthenticationFilter authenticationFilter,
                                                                 final HeadersHolder headersHolder) {
        return new HttpModuleServiceExecutor(authenticationFilter, httpConfig, httpClient(httpConfig), objectMapper, headersHolder);
    }
}
