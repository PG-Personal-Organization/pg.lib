package pg.lib.remote.cqrs.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import pg.lib.cqrs.command.Command;
import pg.lib.cqrs.query.Query;
import pg.lib.remote.cqrs.executors.MissMatchResponseTypeException;
import pg.lib.remote.cqrs.executors.RemoteCqrsModuleServiceExecutor;
import pg.lib.remote.cqrs.executors.RemoteModuleNotFoundException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
public class HttpModuleServiceExecutor implements RemoteCqrsModuleServiceExecutor {
    private final HttpConfig httpConfig;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public <QueryResult, QueryType extends Query<QueryResult>> QueryResult
    execute(final QueryType query, final String module) {
        var request = prepareRequest(query, module, 1);
        var response = executeRequest(request);
        var typeRef = new TypeReference<QueryResult>() {
        };
        return deserializeResult(response.body(), typeRef);
    }

    @Override
    @SneakyThrows
    public <QueryResult, QueryType extends Query<QueryResult>> QueryResult
    execute(QueryType query, String module, int version) {
        var request = prepareRequest(query, module, version);
        var response = executeRequest(request);
        var typeRef = new TypeReference<QueryResult>() {
        };
        return deserializeResult(response.body(), typeRef);
    }

    @Override
    @SneakyThrows
    public <CommandResult, CommandType extends Command<CommandResult>> CommandResult
    execute(final CommandType command, final String module) {
        var request = prepareRequest(command, module, 1);
        var response = executeRequest(request);
        var typeRef = new TypeReference<CommandResult>() {
        };
        return deserializeResult(response.body(), typeRef);
    }

    @Override
    @SneakyThrows
    public <CommandResult, CommandType extends Command<CommandResult>> CommandResult
    execute(CommandType command, String module, int version) {
        var request = prepareRequest(command, module, version);
        var response = executeRequest(request);
        var typeRef = new TypeReference<CommandResult>() {
        };
        return deserializeResult(response.body(), typeRef);
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

        if (Objects.isNull(moduleUrl)) {
            throw new RemoteModuleNotFoundException(module);
        }

        return HttpRequest.newBuilder()
                .uri(new URI(moduleUrl + versionUrl + executableName))
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(commandQuery)))
                .build();
    }

    @SneakyThrows
    private HttpResponse<String> executeRequest(final HttpRequest request) {
        log.info("Sending request: {}", request);
        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Received response: {}", response);
        return response;
    }

    private <T> T deserializeResult(final Object o, final TypeReference<T> dynamicType) {
        try {
            T result = objectMapper.convertValue(o, dynamicType);
            log.debug("Deserialized object: {}", result);
            return result;
        } catch (Exception e) {
            // TODO: 20.11.2023 Check dynamicType.getType().getClass()
            throw new MissMatchResponseTypeException(dynamicType.getType().getClass(), o.getClass());
        }
    }
}
