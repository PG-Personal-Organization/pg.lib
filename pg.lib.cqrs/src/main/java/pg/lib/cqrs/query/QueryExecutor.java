package pg.lib.cqrs.query;


import pg.lib.cqrs.exception.QueryHandlerNotFoundException;

public interface QueryExecutor {
    <QueryResult, QueryType extends Query<QueryResult>>
    QueryResult execute(QueryType query) throws QueryHandlerNotFoundException;
}
