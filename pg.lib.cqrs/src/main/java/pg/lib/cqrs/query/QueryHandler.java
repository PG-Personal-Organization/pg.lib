package pg.lib.cqrs.query;

public interface QueryHandler<Query, QueryResult> {
    QueryResult handle(final Query query);
}
