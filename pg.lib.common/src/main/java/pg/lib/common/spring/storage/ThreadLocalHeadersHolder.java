package pg.lib.common.spring.storage;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The type Thread local header's holder.
 */
@NoArgsConstructor
public class ThreadLocalHeadersHolder implements HeadersHolder {
    private final ThreadLocal<Map<String, String>> headersMap = ThreadLocal.withInitial(HashMap::new);

    @Override
    public void putHeader(final String headerName,final String headerValue) {
        this.headersMap.get().put(headerName, headerValue);
    }

    @Override
    public Optional<String> tryToGetHeader(final String headerName) {
        return Optional.ofNullable(
                this.headersMap.get().get(headerName)
        );
    }
}
