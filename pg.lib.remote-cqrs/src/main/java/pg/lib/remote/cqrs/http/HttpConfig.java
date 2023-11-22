package pg.lib.remote.cqrs.http;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The type Http config.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "remote-cqrs.http")
public class HttpConfig {
    private List<ModuleUrl> remoteModules = Collections.emptyList();
    private Long timeoutInMillis = 60000L;

    /**
     * The type Module url.
     */
    @Data
    @NoArgsConstructor
    public static final class ModuleUrl {
        private String moduleName;
        private String url;
    }
}