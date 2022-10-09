package pg.lib.cqrs.query;

import lombok.AllArgsConstructor;
import pg.lib.cqrs.exception.QueryHandlerNotFoundException;

import java.util.List;

@AllArgsConstructor
public class DefaultQueryExecutor implements QueryExecutor {

    private final List<QueryHandler<Query<?>, ?>> queryHandlers;

    public <T> T execute(final Query<T> query) throws QueryHandlerNotFoundException {

//        Optional<QueryHandler<Query<T>, T>> queryHandler = queryHandlers.stream()
//                .filter(handler -> handler.getClass().getGenericSuperclass().)
//                .findFirst();
//
//        if (queryHandler.isEmpty())
//            throw new QueryHandlerNotFoundException(String.format("Command Handler for Command: %s - not found", query.getClass()));
//
//        return queryHandler.get().handle(query);

        return null;
    }
}
