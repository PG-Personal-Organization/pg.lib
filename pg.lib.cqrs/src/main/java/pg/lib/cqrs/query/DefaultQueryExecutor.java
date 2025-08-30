package pg.lib.cqrs.query;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.ObjectProvider;
import pg.lib.cqrs.exception.QueryHandlerNotFoundException;
import pg.lib.cqrs.util.ClassUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * Default implementation of QueryExecutor that can eagerly register known QueryHandlers
 * and lazily resolve missing ones from the Spring context on first use.
 */
@Log4j2
@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultQueryExecutor implements QueryExecutor {
    private final Map<Class<?>, QueryHandler> queryHandlers = new HashMap<>();
    private final ObjectProvider<List<QueryHandler>> queryHandlersProvider;

    @SuppressWarnings("checkstyle:HiddenField")
    public DefaultQueryExecutor(final ObjectProvider<List<QueryHandler>> queryHandlersProvider) {
        this.queryHandlersProvider = queryHandlersProvider;
        var initialHandlers = queryHandlersProvider.getIfAvailable(List::of);

        if (!initialHandlers.isEmpty()) {
            log.debug("Registering {} QueryHandler beans eagerly", initialHandlers.size());
            initialHandlers.forEach(this::addQueryHandler);
            log.debug("Initial QueryHandlers registered");
        }
    }

    @Override
    public <QueryResult, QueryType extends Query<QueryResult>>
    QueryResult execute(final QueryType query) {
        Class<?> queryClass = query.getClass();
        QueryHandler<QueryType, QueryResult> queryHandler = queryHandlers.get(queryClass);

        if (isNull(queryHandler)) {
            log.info("QueryHandler for {} not found in cache, attempting to resolve from Spring context", queryClass);

            queryHandler = resolveHandler(queryClass);

            if (isNull(queryHandler)) {
                throw new QueryHandlerNotFoundException(
                        "QueryHandler for query type %s not found".formatted(queryClass.getName())
                );
            }

            // cache for next use
            queryHandlers.put(queryClass, queryHandler);
            log.info("Resolved and cached QueryHandler {} for {}", queryHandler.getClass(), queryClass);
        }

        return queryHandler.handle(query);
    }

    private void addQueryHandler(final QueryHandler handler) {
        Class<?> queryType = ClassUtils.findInterfaceParameterType(handler.getClass(), QueryHandler.class, 0);
        log.debug("Registering handler {} for query type {}", handler.getClass().getSimpleName(), queryType.getSimpleName());
        queryHandlers.put(queryType, handler);
    }

    @SuppressWarnings("unchecked")
    private <QueryResult, QueryType extends Query<QueryResult>>
    QueryHandler<QueryType, QueryResult> resolveHandler(final Class<?> queryClass) {
        return queryHandlersProvider.getIfAvailable(List::of).stream()
                .filter(handler ->
                        ClassUtils.findInterfaceParameterType(handler.getClass(), QueryHandler.class, 0)
                                .equals(queryClass))
                .findFirst()
                .orElse(null);
    }
}
