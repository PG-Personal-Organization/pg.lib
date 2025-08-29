package pg.lib.common.spring.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import pg.lib.common.spring.storage.HeadersHolder;

import java.io.IOException;

@Log4j2
public class NoOpHeaderAuthenticationFilter extends HeaderAuthenticationFilter {

    /**
     * Instantiates a new Header authentication filter.
     *
     * @param headersHolder the headers holder
     */
    public NoOpHeaderAuthenticationFilter(final @NonNull HeadersHolder headersHolder) {
        super(headersHolder);
    }

    @Override
    protected void doFilterInternal(final @NonNull HttpServletRequest request,
                                    final @NonNull HttpServletResponse response,
                                    final @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.debug("NoOpHeaderAuthenticationFilter is active. No authentication will be performed.");
        filterChain.doFilter(request, response);
    }

    @Override
    protected void continueFilter(final HttpServletRequest request,
                                  final HttpServletResponse response,
                                  final FilterChain filterChain,
                                  final String authenticationToken) throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }
}
