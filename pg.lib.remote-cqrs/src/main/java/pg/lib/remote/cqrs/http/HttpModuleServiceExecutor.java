package pg.lib.remote.cqrs.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import pg.lib.common.spring.auth.HeaderAuthenticationFilter;
import pg.lib.common.spring.auth.HeaderNames;
import pg.lib.cqrs.command.Command;
import pg.lib.cqrs.query.Query;
import pg.lib.cqrs.util.ClassUtils;
import pg.lib.remote.cqrs.executors.MissMatchResponseTypeException;
import pg.lib.remote.cqrs.executors.RemoteCqrsModuleServiceExecutor;
import pg.lib.remote.cqrs.executors.RemoteModuleNotFoundException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

/**
 * The type Http module service executor.
 */
@Log4j2
public class HttpModuleServiceExecutor implements RemoteCqrsModuleServiceExecutor {
    private final HeaderAuthenticationFilter authenticator;
    private final HttpConfig httpConfig;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    /**
     * Instantiates a new Http module service executor.
     *
     * @param authenticator the authenticator
     * @param httpConfig    the http config
     * @param client        the client
     * @param objectMapper  the object mapper
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public HttpModuleServiceExecutor(final @Lazy HeaderAuthenticationFilter authenticator, final HttpConfig httpConfig,
                                     final HttpClient client, final ObjectMapper objectMapper) {
        this.authenticator = authenticator;
        this.httpConfig = httpConfig;
        this.client = client;
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows
    public <QueryResult, QueryType extends Query<QueryResult>> QueryResult
    execute(final QueryType query, final String module) {
        var request = prepareRequest(query, module, 1);
        var response = executeRequest(request);
        var clazz = provideQueryReturnClass(query.getClass());
        return deserializeResult(response.body(), clazz);
    }

    @Override
    @SneakyThrows
    public <QueryResult, QueryType extends Query<QueryResult>> QueryResult
    execute(final QueryType query, final String module, final int version) {
        var request = prepareRequest(query, module, version);
        var response = executeRequest(request);
        var clazz = provideQueryReturnClass(query.getClass());
        return deserializeResult(response.body(), clazz);
    }

    @Override
    @SneakyThrows
    public <CommandResult, CommandType extends Command<CommandResult>> CommandResult
    execute(final CommandType command, final String module) {
        var request = prepareRequest(command, module, 1);
        var response = executeRequest(request);
        var clazz = provideCommandReturnClass(command.getClass());
        return deserializeResult(response.body(), clazz);
    }

    @Override
    @SneakyThrows
    public <CommandResult, CommandType extends Command<CommandResult>> CommandResult
    execute(final CommandType command, final String module, final int version) {
        var request = prepareRequest(command, module, version);
        var response = executeRequest(request);
        var clazz = provideCommandReturnClass(command.getClass());
        return deserializeResult(response.body(), clazz);
    }

    @SneakyThrows
    private HttpRequest prepareRequest(final Object commandQuery, final @NonNull String module, final Integer version) {
        var moduleUrl = httpConfig.getRemoteModules().stream()
                .filter(rm -> rm.getModuleName().equals(module))
                .findFirst()
                .map(HttpConfig.ModuleUrl::getUrl)
                .orElse(null);
        var versionUrl = version != null ? "/v" + version : "";
        var executableName = "/" + commandQuery.getClass().getSimpleName();
        var authToken = authenticator.getAuthenticationToken();

        if (Objects.isNull(moduleUrl)) {
            throw new RemoteModuleNotFoundException(module);
        }

        if (Objects.isNull(authToken)) {
            log.warn("Provided authentication token is null!");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(moduleUrl + versionUrl + executableName))
                .header("Content-Type", "application/json; charset=UTF-8")
                .header(HeaderNames.CONTEXT_TOKEN, authToken)
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(commandQuery)))
                .build();
        log.info("Prepared request: {} with body: {} and authToken: {}", request, commandQuery, authToken);
        return request;
    }

    @SneakyThrows
    private HttpResponse<String> executeRequest(final HttpRequest request) {
        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Received response: {} {}", response, response.body());
        return response;
    }

    private Class<?> provideQueryReturnClass(final Class<?> query) {
        return ClassUtils.findInterfaceParameterType(query, Query.class, 0);
    }

    private Class<?> provideCommandReturnClass(final Class<?> command) {
        return ClassUtils.findInterfaceParameterType(command, Command.class, 0);
    }

    private <T> T deserializeResult(final String o, final Class<?> clazz) {
        try {
            T result = (T) objectMapper.readValue(o, clazz);
            log.info("Deserialized response object from: {} to: {}", o, result);
            return result;
        } catch (Exception e) {
            throw new MissMatchResponseTypeException(clazz, o.getClass());
        }
    }
}
