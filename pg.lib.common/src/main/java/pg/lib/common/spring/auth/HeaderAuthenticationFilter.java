package pg.lib.common.spring.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import pg.lib.common.spring.storage.HeadersHolder;

import java.io.IOException;

/**
 * The interface Header authentication filter.
 */
public abstract class HeaderAuthenticationFilter extends OncePerRequestFilter {
    /**
     * The Headers holder.
     */
    protected final HeadersHolder headersHolder;

    /**
     * Instantiates a new Header authentication filter.
     *
     * @param headersHolder the headers holder
     */
    @SuppressWarnings("checkstyle:HiddenField")
    protected HeaderAuthenticationFilter(final @Lazy @NonNull HeadersHolder headersHolder) {
        this.headersHolder = headersHolder;
    }

    /**
     * Continue filter.
     *
     * @param request             the request
     * @param response            the response
     * @param filterChain         the filter chain
     * @param authenticationToken the authentication token
     * @throws ServletException the servlet exception
     * @throws IOException      the io exception
     */
    protected abstract void continueFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
                                           String authenticationToken) throws ServletException, IOException;

    @Override
    protected void doFilterInternal(final @NonNull HttpServletRequest request,
                                    final @NonNull HttpServletResponse response,
                                    final @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authenticationToken = request.getHeader(HeaderNames.CONTEXT_TOKEN);

        if (!(authenticationToken == null || authenticationToken.isBlank())) {
            storeAuthenticationToken(authenticationToken);

            continueFilter(request, response, filterChain, authenticationToken);

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    String.format("Authorization header: %s has been not provided", HeaderNames.CONTEXT_TOKEN));
        }

        filterChain.doFilter(request, response);

    }

    /**
     * Store authentication token.
     *
     * @param authenticationToken the authentication token
     */
    protected void storeAuthenticationToken(final String authenticationToken) {
        this.headersHolder.putHeader(HeaderNames.CONTEXT_TOKEN, authenticationToken);
    }

    /**
     * Gets authentication token.
     *
     * @return the authentication token
     */
    public String getAuthenticationToken() {
        return headersHolder.tryToGetHeader(HeaderNames.CONTEXT_TOKEN)
                .orElse(null);
    }
}
