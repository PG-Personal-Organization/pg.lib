package pg.lib.cqrs.query;

public interface QueryHandler<QueryType extends Query<QueryResult>, QueryResult> {
    QueryResult handle(final QueryType query);
}
