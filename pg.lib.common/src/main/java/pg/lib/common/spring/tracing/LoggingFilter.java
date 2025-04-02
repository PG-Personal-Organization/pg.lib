package pg.lib.common.spring.tracing;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;
import pg.lib.common.spring.auth.HeaderNames;
import pg.lib.common.spring.storage.HeadersHolder;

import java.io.IOException;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
public class LoggingFilter extends OncePerRequestFilter {
    private final HeadersHolder headersHolder;

    @Override
    protected void doFilterInternal(final @NonNull HttpServletRequest request, final @NonNull HttpServletResponse response,
                                    final @NonNull FilterChain filterChain) throws ServletException, IOException {
        headersHolder.putHeader(HeaderNames.TRACE_ID, UUID.randomUUID().toString());
        filterChain.doFilter(request, response);
    }
}
