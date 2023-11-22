package pg.lib.cqrs.query;


import pg.lib.cqrs.exception.QueryHandlerNotFoundException;

/**
 * The interface Query executor.
 */
public interface QueryExecutor {
    /**
     * Execute query result.
     *
     * @param <QueryResult> the type parameter
     * @param <QueryType>   the type parameter
     * @param query         the query
     * @return the query result
     * @throws QueryHandlerNotFoundException the query handler not found exception
     */
    <QueryResult, QueryType extends Query<QueryResult>>
    QueryResult execute(QueryType query) throws QueryHandlerNotFoundException;
}
