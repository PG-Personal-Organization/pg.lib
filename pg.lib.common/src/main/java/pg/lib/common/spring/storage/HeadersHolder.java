package pg.lib.common.spring.storage;

import java.util.Optional;

/**
 * The interface Headers holder.
 */
public interface HeadersHolder {
    /**
     * Put header.
     *
     * @param headerName  the header name
     * @param headerValue the header value
     */
    void putHeader(String headerName, String headerValue);

    /**
     * Try to get header optional.
     *
     * @param headerName the header name
     * @return the optional
     */
    Optional<String> tryToGetHeader(String headerName);
}
