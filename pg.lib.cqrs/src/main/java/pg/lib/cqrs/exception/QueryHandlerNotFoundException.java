package pg.lib.cqrs.exception;

/**
 * The type Query handler not found exception.
 */
public class QueryHandlerNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Query handler not found exception.
     *
     * @param message the message
     */
    public QueryHandlerNotFoundException(final String message) {
        super(message);
    }
}
