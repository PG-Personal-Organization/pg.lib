package pg.lib.cqrs.query;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import pg.lib.cqrs.exception.QueryHandlerNotFoundException;
import pg.lib.cqrs.util.ClassUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * The type Default query executor.
 */
@Log4j2
@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultQueryExecutor implements QueryExecutor {
    private final Map<Class<?>, QueryHandler> queryHandlers;
    private final boolean canLog;

    /**
     * Instantiates a new Default query executor.
     *
     * @param queryHandlers the query handlers
     * @param env           the env
     */
    public DefaultQueryExecutor(final Collection<QueryHandler> queryHandlers, final Environment env) {
        this.queryHandlers = new HashMap<>();

        canLog = Arrays.stream(env.getActiveProfiles()).toList().contains("devlocal");

        if (!queryHandlers.isEmpty()) {
            if (canLog) {
                log.info("------------------ Registering found QueryHandler beans --------------------------\n");
            }
            queryHandlers.forEach(this::addQueryHandler);
            if (canLog) {
                log.info("------------------ Registering QueryHandlers completed  --------------------------");
            }
        }
    }

    @Override
    public <QueryResult, QueryType extends Query<QueryResult>>
    QueryResult execute(final QueryType query) throws QueryHandlerNotFoundException {
        final QueryHandler<QueryType, QueryResult> queryHandler = queryHandlers.get(query.getClass());

        if (isNull(queryHandler)) {
            throw new QueryHandlerNotFoundException("Query Handler for Query: %s - not found".formatted(query.getClass()));
        }

        return queryHandler.handle(query);
    }

    private void addQueryHandler(final QueryHandler handler) {
        if (canLog) {
            log.info("QueryHandler: %s%n".formatted(handler.getClass()));
        }
        this.queryHandlers.put(ClassUtils.findInterfaceParameterType(handler.getClass(), QueryHandler.class, 0), handler);
    }
}
