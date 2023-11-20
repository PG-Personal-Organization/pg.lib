package pg.lib.remote.cqrs.executors;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
@NoArgsConstructor
public class HttpConfig {
    @Value(value = "${remote-cqrs.http.remote.modules:}#{T(java.util.Collections).emptyMap()}")
    private Map<String, String> remoteModules;

    @Value("${remote-cqrs.http.timeout:60000}")
    private Integer timeoutInMillis;
}