package pg.lib.remote.cqrs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pg.lib.remote.cqrs.executors.RemoteCqrsModuleServiceExecutor;
import pg.lib.remote.cqrs.http.HttpConfig;
import pg.lib.remote.cqrs.http.HttpModuleServiceExecutor;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
@Import({HttpConfig.class})
public class RemoteModulesCqrsConfiguration {
    @Bean
    public HttpClient httpClient(final HttpConfig httpConfig) {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofMillis(httpConfig.getTimeoutInMillis()))
                .build();
    }

    @Bean
    public RemoteCqrsModuleServiceExecutor remoteServiceExecutor(final HttpConfig httpConfig, final ObjectMapper objectMapper) {
        return new HttpModuleServiceExecutor(httpConfig, httpClient(httpConfig), objectMapper);
    }
}
