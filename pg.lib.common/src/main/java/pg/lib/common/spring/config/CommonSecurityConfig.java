package pg.lib.common.spring.config;

import lombok.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.authentication.AuthenticationManagerBeanDefinitionParser;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pg.lib.common.spring.auth.HeaderAuthenticationFilter;
import pg.lib.common.spring.exception.InvalidRequestCustomizerException;
import pg.lib.common.spring.exception.MissingValidHeaderAuthenticationFilterException;
import pg.lib.common.spring.storage.HeadersHolder;
import pg.lib.common.spring.storage.ThreadLocalHeadersHolder;
import pg.lib.common.spring.user.Roles;

import java.util.Arrays;
import java.util.Collection;

/**
 * The type Common security config.
 */
@Configuration
public class CommonSecurityConfig {
    private static final int ENCRYPT_STRENGTH = 8;

    /**
     * Method security expression handler method security expression handler.
     *
     * @param roleHierarchy the role hierarchy
     * @return the method security expression handlerRoles
     */
    @Bean
    public static @NonNull MethodSecurityExpressionHandler methodSecurityExpressionHandler(final @NonNull RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    /**
     * B crypt password encoder b crypt password encoder.
     *
     * @return the b crypt password encoder
     */
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(ENCRYPT_STRENGTH);
    }

    /**
     * Cors configuration source cors configuration source.
     *
     * @return the cors configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Role hierarchy.
     *
     * @return the role hierarchy
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = String.format("ROLE_%s > ROLE_%s", Roles.ADMIN.name(), Roles.USER.name());
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    /**
     * Authentication filter header authentication filter.
     *
     * @return the header authentication filter
     */
    @Bean
    @ConditionalOnMissingBean
    public HeaderAuthenticationFilter authenticationFilter() {
        throw new MissingValidHeaderAuthenticationFilterException();
    }

    /**
     * Security filter chain security filter chain.
     *
     * @param http                       the http
     * @param corsConfigurationSource    the cors configuration source
     * @param requestPermits             the request permits
     * @param headerAuthenticationFilter the header authentication filter
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    @Primary
    public SecurityFilterChain securityFilterChain(
            final @NonNull HttpSecurity http,
            final @NonNull CorsConfigurationSource corsConfigurationSource,
            final @NonNull Collection<Customizer<
                    AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>> requestPermits,
            final @NonNull HeaderAuthenticationFilter headerAuthenticationFilter) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(headerAuthenticationFilter, AnonymousAuthenticationFilter.class)

                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/actuator/**", "/swagger-ui/**", "/swagger-ui.html**", "/v3/api-docs/**").permitAll());

        requestPermits.forEach(permit -> {
            try {
                http.authorizeHttpRequests(permit);
            } catch (final Exception e) {
                throw new InvalidRequestCustomizerException(e);
            }
        });

        return http.build();
    }

    /**
     * Null authentication provider.
     *
     * @return the authentication provider
     */
    @Bean
    public AuthenticationProvider nullAuthenticationProvider() {
        return new AuthenticationManagerBeanDefinitionParser.NullAuthenticationProvider();
    }

    /**
     * Thread local header's holder.
     *
     * @return the headers holder
     */
    @Bean
    public HeadersHolder threadLocalHeadersHolder() {
        return new ThreadLocalHeadersHolder();
    }
}
