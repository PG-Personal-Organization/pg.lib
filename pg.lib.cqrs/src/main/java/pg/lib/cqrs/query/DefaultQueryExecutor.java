package pg.lib.cqrs.query;

import lombok.extern.slf4j.Slf4j;

import pg.lib.cqrs.exception.QueryHandlerNotFoundException;
import pg.lib.cqrs.util.ClassUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultQueryExecutor implements QueryExecutor {

    private final Map<Class<?>, QueryHandler> queryHandlers;

    public DefaultQueryExecutor(final Collection<QueryHandler> queryHandlers) {
        this.queryHandlers = new HashMap<>();

        if (!queryHandlers.isEmpty()) {
            log.info("------------------ Registering found QueryHandler beans --------------------------\n");
            queryHandlers.forEach(this::addQueryHandler);
            log.info("------------------ Registering QueryHandlers completed  --------------------------");
        }
    }

    @Override
    public <QueryResult, QueryType extends Query<QueryResult>>
    QueryResult execute(QueryType query) throws QueryHandlerNotFoundException {
        final QueryHandler<QueryType, QueryResult> queryHandler = queryHandlers.get(query.getClass());

        if (isNull(queryHandler)) {
            throw new QueryHandlerNotFoundException("Query Handler for Query: %s - not found".formatted(query.getClass()));
        }

        return queryHandler.handle(query);
    }

    private void addQueryHandler(final QueryHandler handler) {
        log.info("QueryHandler: %s%n".formatted(handler.getClass()));
        this.queryHandlers.put(ClassUtils.findInterfaceParameterType(handler.getClass(), QueryHandler.class, 0), handler);
    }
}
