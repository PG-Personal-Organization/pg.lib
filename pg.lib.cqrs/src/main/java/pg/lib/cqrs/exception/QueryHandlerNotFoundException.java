package pg.lib.cqrs.exception;

public class QueryHandlerNotFoundException extends RuntimeException {
    public QueryHandlerNotFoundException(String message) {
        super(message);
    }
}
