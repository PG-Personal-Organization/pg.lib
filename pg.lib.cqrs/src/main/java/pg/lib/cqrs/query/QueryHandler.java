package pg.lib.cqrs.query;

/**
 * The interface Query handler.
 *
 * @param <QueryType>   the type parameter
 * @param <QueryResult> the type parameter
 */
public interface QueryHandler<QueryType extends Query<QueryResult>, QueryResult> {
    /**
     * Handle query result.
     *
     * @param query the query
     * @return the query result
     */
    QueryResult handle(final QueryType query);
}
