package pg.lib.cqrs.query;


import pg.lib.cqrs.exception.QueryHandlerNotFoundException;

public interface QueryExecutor {
    <T> T execute(Query<T> query) throws QueryHandlerNotFoundException;
}
